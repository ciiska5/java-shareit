package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.BookingBadRequest;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addNewItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User user = checkUsersExistenceById(userId);
        item.setOwner(user);
        itemRepository.save(item);
        log.info("Пользователем с id = {} добавлена вещь c id = {}.", userId, item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        List<Item> userItems = itemRepository.findAllItemsByOwnerId(userId);
        if (userItems == null) {
            log.error("У пользователя с id = {} нет вещей для аренды. ", userId);
            throw new ItemNotFoundException("У пользователя с id = " + userId + " нет вещей для аренды.");
        }
        checkUsersExistenceById(userId);
        Item updatingItem = checkItemsExistenceById(itemId);
        String itemName = itemDto.getName();
        String itemDescription = itemDto.getDescription();
        Boolean itemAvailable = itemDto.getAvailable();
        if (itemName != null) {
            updatingItem.setName(itemName);
        }
        if (itemDescription != null) {
            updatingItem.setDescription(itemDescription);
        }
        if (itemAvailable != null) {
            updatingItem.setAvailable(itemAvailable);
        }
        log.info("У пользователя с id = {} обновлена вещь с id = {}. ", userId, itemId);

        return ItemMapper.toItemDto(itemRepository.save(updatingItem));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDateDto getItemById(Long userId, Long itemId) {
        Item item = checkItemsExistenceById(itemId);
        ItemDateDto itemDateDto = ItemMapper.toItemDateDto(item);

        if (item.getOwner().getId().equals(userId)) {
            List<Booking> ascBookingList = bookingRepository.findAllByItemIdAndStatusEqualsOrderByStartAsc(
                    itemId, BookingStatus.APPROVED);
            if (!ascBookingList.isEmpty()) {
                int bookingListSize = ascBookingList.size();
                if (bookingListSize == 1) {
                    if (ascBookingList.get(0).getStart().isBefore(LocalDateTime.now())) {
                        itemDateDto.setLastBooking(BookingMapper.toBookingDateDto(ascBookingList.get(0)));
                        itemDateDto.setNextBooking(null);
                    } else {
                        itemDateDto.setLastBooking(null);
                        itemDateDto.setNextBooking(BookingMapper.toBookingDateDto(ascBookingList.get(0)));
                    }
                } else {
                    int i = -1;
                    do {
                        i++;
                    } while (ascBookingList.get(i).getStart().isBefore(LocalDateTime.now()));
                    itemDateDto.setNextBooking(BookingMapper.toBookingDateDto(ascBookingList.get(i)));
                    itemDateDto.setLastBooking(BookingMapper.toBookingDateDto(ascBookingList.get(i - 1)));
                }
            } else {
                itemDateDto.setLastBooking(null);
                itemDateDto.setNextBooking(null);
            }
        }

        List<CommentDto> itemCommentsList =
                commentRepository.findAllByItemId(itemId)
                        .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        itemDateDto.setComments(itemCommentsList);

        log.info("Пользователем userId = {} получена вещь с id = {}.", userId, itemId);
        return itemDateDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDateDto> getAllItemsOfUser(Long userId) {
        checkUsersExistenceById(userId);
        List<Item> userItems = itemRepository.findAllItemsByOwnerId(userId);
        List<ItemDateDto> userItemsDateDto = new ArrayList<>();
        userItems.forEach(item -> userItemsDateDto.add(ItemMapper.toItemDateDto(item)));


        userItemsDateDto.forEach(itemDateDto -> {
            Long itemId = itemDateDto.getId();
            List<Booking> ascBookingList = bookingRepository.findAllByItemIdOrderByStartAsc(itemId);
            itemDateDto.setLastBooking(
                    ascBookingList.isEmpty() ? null : BookingMapper.toBookingDateDto(ascBookingList.get(0))
            );
            itemDateDto.setNextBooking(
                    itemDateDto.getLastBooking() == null ||
                            ascBookingList.size() == 1 ? null : BookingMapper.toBookingDateDto(ascBookingList.get(1))
            );
            List<Comment> itemCommentsList = commentRepository.findAllByItemId(itemId);
            itemDateDto.setComments(
                    itemCommentsList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList())
            );
        });

        log.info("Получены все вещи пользователя-владельца с id = {}", userId);
        return userItemsDateDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByRequestText(Long userId, String text) {
        checkUsersExistenceById(userId);
        List<Item> items = itemRepository.findAll();
        List<Item> foundItems = new ArrayList<>();

        if (!text.isEmpty()) {
            String lowerCaseText = text.toLowerCase();
            foundItems = items
                    .stream()
                    .filter(item -> item.getAvailable().equals(Boolean.TRUE) &&
                            ((item.getName().toLowerCase().contains(lowerCaseText)) ||
                                    item.getDescription().toLowerCase().contains(lowerCaseText)))
                    .collect(Collectors.toList());

            if (foundItems.isEmpty()) {
                log.info("Для пользователя с id = {} по его запросу \"{}\" ничего не найдено.", userId, text);
            } else {
                log.info("Для пользователя с id = {} по его запросу \"{}\" количество найденных результатов равно {}",
                        userId, text, foundItems.size());
            }
        } else {
            log.error("Пользователем с id = {} задан пустой поисковый запрос.", userId);
        }

        List<ItemDto> foundItemsDto = new ArrayList<>();
        foundItems.forEach(item -> foundItemsDto.add(ItemMapper.toItemDto(item)));
        return foundItemsDto;
    }

    @Override
    @Transactional
    public CommentDto addNewCommentForItem(Long userId, Long itemId, CommentDto commentDto) {
        User user = checkUsersExistenceById(userId);
        Item item = checkItemsExistenceById(itemId);

        List<Booking> userBookingList =
                bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        userId, itemId, BookingStatus.APPROVED, LocalDateTime.now()
                );

        if (userBookingList.isEmpty()) {
            log.error("Пользователь с id = {} не арендовывал вещь с id = {}. ", userId, itemId);
            throw new BookingBadRequest(
                    "Пользователь с id = " + userId + " не арендовывал вещь с id = " + itemId + "."
            );
        }

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        log.info("Пользователь с id = {} оставил отзыв на вещь с id = {}. ", userId, itemId);
        return CommentMapper.toCommentDto(comment);
    }

    //проверка пользователя на существование
    private User checkUsersExistenceById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден."));
    }

    //проверка вещи на существование
    private Item checkItemsExistenceById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + itemId + " не найден."));
    }
}
