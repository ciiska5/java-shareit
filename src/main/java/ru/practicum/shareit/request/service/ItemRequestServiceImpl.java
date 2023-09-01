package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.PaginationParamException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    //добавить новый запрос вещи
    @Override
    @Transactional
    public ItemRequestDto addNewRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = checkUsersExistenceById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequestRepository.save(itemRequest);
        log.info("Пользователь с id = {} добавил новый запрос на вещь. ", userId);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    //получить список своих запросов вместе с данными об ответах на них от более новых к более старым
    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllByRequestor(Long userId) {
        checkUsersExistenceById(userId);
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        itemRequestDtoList.forEach(this::addItemListToItemRequestDto);

        log.info("Пользователь с id = {} получил список всех своих запросов. ", userId);
        return itemRequestDtoList;
    }

    //получить постранично список запросов, созданных другими пользователями от более новых к более старым.
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        User user = checkUsersExistenceById(userId);
        if (from < 0 || size <= 0) {
            throw new PaginationParamException("Некорректно заданы параметры пагинации");
        }
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository
                .findAllByRequestorNotLikeOrderByCreatedAsc(user, PageRequest.of(from, size))
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        itemRequestDtoList.forEach(this::addItemListToItemRequestDto);

        log.info("Получен список всех запросов пользователя с id = {} . ", userId);
        return itemRequestDtoList;
    }

    //получить данные об одном конкретном запросе вместе с данными об ответах на него
    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        checkUsersExistenceById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Запрос с id = " + requestId + " не найден."));

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        addItemListToItemRequestDto(itemRequestDto);

        log.info("Пользователем с id = {} получен запрос с requestId = {}. ", userId, requestId);
        return itemRequestDto;
    }

    //проверка пользователя на существование
    private User checkUsersExistenceById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден."));
    }

    //добавление списка вещей (ответов) к запросу
    private void addItemListToItemRequestDto (ItemRequestDto itemRequestDto) {
        itemRequestDto.setItems(itemRepository.findAllByRequestId(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())
        );
    }
}
