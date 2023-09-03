package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

public class ItemServiceImplTest {

    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    private final CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);

    private ItemServiceImpl itemService;
    private Item testItem;
    private User testUser;

    @BeforeEach
    void initItem() {
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Sword");
        testItem.setDescription("Sharp sword for enemies");
        testItem.setOwner(new User());
        testItem.setAvailable(Boolean.TRUE);
        testItem.setRequest(new ItemRequest());
    }

    @BeforeEach
    void initUser() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Altair");
        testUser.setEmail("matterLabs@gmail.com");
    }

    @BeforeEach
    void initItemService() {
        itemService = new ItemServiceImpl(
                mockItemRepository,
                mockUserRepository,
                mockBookingRepository,
                mockCommentRepository,
                mockItemRequestRepository
        );
    }

    @Test
    void addNewItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRepository.save(any(Item.class)))
                .thenReturn(testItem);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        ItemDto savedItemDto = itemService.addNewItem(testItemDto, 1L);

        Assertions.assertEquals(testItemDto, savedItemDto);
    }

    @Test
    void addNewItemByNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemService.addNewItem(testItemDto, 2L)
        );

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void addNewItemToRequestTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(new ItemRequest()));
        Mockito
                .when(mockItemRepository.save(any(Item.class)))
                .thenReturn(testItem);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        ItemDto savedItemDto = itemService.addNewItem(testItemDto, 1L);

        Assertions.assertEquals(testItemDto, savedItemDto);
    }

    @Test
    void addNewItemToNotExistingRequestTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        testItemDto.setRequestId(100L);

        ItemRequestNotFoundException error = Assertions.assertThrows(
                ItemRequestNotFoundException.class, () -> itemService.addNewItem(testItemDto, 2L)
        );

        Assertions.assertEquals("Запрос с id = 100 не найден.", error.getMessage());
    }

    @Test
    void updateItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        testItem.setOwner(testUser);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        testItemDto.setName("Renewed sword");
        testItemDto.setDescription("Sharp sword with lighter handle");
        testItemDto.setAvailable(Boolean.FALSE);
        Mockito
                .when(mockItemRepository.save(any(Item.class)))
                .thenReturn(testItem);

        ItemDto updatedItemDto = itemService.updateItem(testItemDto, 1L, 1L);

        Assertions.assertEquals(testItemDto, updatedItemDto);
        Assertions.assertEquals(Boolean.FALSE, updatedItemDto.getAvailable());
        Assertions.assertEquals("Renewed sword", updatedItemDto.getName());
        Assertions.assertEquals("Sharp sword with lighter handle", updatedItemDto.getDescription());
    }

    @Test
    void updateItemOfNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemService.updateItem(testItemDto, 2L, 3L)
        );

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void updateNotExistingItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> itemService.updateItem(testItemDto, 2L, 3L)
        );

        Assertions.assertEquals("Вещь с id = 3 не найдена.", error.getMessage());
    }

    @Test
    void updateItemByNotOwnerTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        testItem.setOwner(testUser);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> itemService.updateItem(testItemDto, 2L, 1L)
        );

        Assertions.assertEquals("У пользователя с id = 2 нет вещи для аренды с id = 1", error.getMessage());
    }

    @Test
    void getItemByIdByOwnerTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));

        testItem.setOwner(testUser);
        ItemDateDto testItemDateDto = ItemMapper.toItemDateDto(testItem);
        testItemDateDto.setComments(List.of());

        User testBooker = new User();
        testBooker.setId(2L);
        testBooker.setName("Ezio");
        testBooker.setEmail("starkWare@gmail.com");

        Booking testBooking = new Booking();
        testBooking.setItem(testItem);
        testBooking.setBooker(testBooker);
        testBooking.setStart(LocalDateTime.now());

        BookingDateDto bookingDateDto = BookingMapper.toBookingDateDto(testBooking);
        testItemDateDto.setLastBooking(bookingDateDto);

        Mockito
                .when(mockBookingRepository.findAllByItemIdAndStatusEqualsOrderByStartAsc(
                        anyLong(), any(BookingStatus.class))
                )
                .thenReturn(List.of(testBooking));
        Mockito
                .when(mockCommentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of());

        ItemDateDto foundItemDateDto = itemService.getItemById(testItem.getOwner().getId(), 1L);

        Assertions.assertEquals(foundItemDateDto, testItemDateDto);
        Assertions.assertNotNull(foundItemDateDto.getLastBooking());
        Assertions.assertNull(foundItemDateDto.getNextBooking());
    }

    @Test
    void getItemByIdByNotOwnerTest() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        testItem.setOwner(testUser);
        ItemDateDto testItemDateDto = ItemMapper.toItemDateDto(testItem);
        testItemDateDto.setComments(List.of());
        Mockito
                .when(mockCommentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of());

        ItemDateDto foundItemDateDto = itemService.getItemById(testItem.getOwner().getId() + 1L, 1L);

        Assertions.assertEquals(foundItemDateDto, testItemDateDto);
        Assertions.assertNull(foundItemDateDto.getLastBooking());
    }

    @Test
    void getItemByNotExistingId() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> itemService.getItemById(1L, 1L)
        );

        Assertions.assertEquals("Вещь с id = 1 не найдена.", error.getMessage());
    }

    @Test
    void getAllItemsOfUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findAllItemsByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testItem)));
        Mockito
                .when(mockBookingRepository.findAllByItemOwnerIdOrderByStartAsc(anyLong()))
                .thenReturn(List.of());
        Mockito
                .when(mockCommentRepository.findAllByItemIn(anyList()))
                .thenReturn(List.of());
        ItemDateDto testItemDateDto = ItemMapper.toItemDateDto(testItem);
        testItemDateDto.setComments(List.of());
        List<ItemDateDto> testItemDateDtoList = List.of(testItemDateDto);


        List<ItemDateDto> foundItemDateDtoList = itemService.getAllItemsOfUser(1L, 0, 1);

        Assertions.assertEquals(testItemDateDtoList, foundItemDateDtoList);
    }

    @Test
    void getAllItemsOfNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemService.getAllItemsOfUser(1L, 0, 1)
        );

        Assertions.assertEquals("Пользователь с id = 1 не найден.", error.getMessage());
    }

    @Test
    void getAllItemsOfUserWithIncorrectPaginationParamsTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));

        Exception error = Assertions.assertThrows(
                Exception.class, () -> itemService.getAllItemsOfUser(2L, -1, 0));

        Assertions.assertEquals("Page index must not be less than zero", error.getMessage());
    }

    @Test
    void getAllItemsByRequestTextTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findAllItemsByText(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testItem)));

        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        List<ItemDto> testItemDtoList = List.of(testItemDto);

        List<ItemDto> foundItemDtoList = itemService.getItemsByRequestText(1L, "sword", 0, 1);

        Assertions.assertEquals(testItemDtoList, foundItemDtoList);
        Assertions.assertEquals(foundItemDtoList.size(), 1);
    }

    @Test
    void getAllItemsByEmptyRequestTextTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));

        List<ItemDto> foundItemDtoList = itemService.getItemsByRequestText(1L, "", 0, 1);

        Assertions.assertEquals(foundItemDtoList.size(), 0);
    }

    @Test
    void getAllItemsByNotExistingRequestTextTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findAllItemsByText(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        List<ItemDto> foundItemDtoList = itemService.getItemsByRequestText(1L, "test", 0, 1);

        Assertions.assertEquals(foundItemDtoList.size(), 0);
    }

    @Test
    void getAllItemsByTextWithIncorrectPaginationParamsTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));

        Exception error = Assertions.assertThrows(
                Exception.class, () -> itemService.getItemsByRequestText(
                        2L, "t", -1, 0)
        );

        Assertions.assertEquals("Page index must not be less than zero", error.getMessage());
    }

    @Test
    void addNewCommentForItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));

        Booking testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setStart(LocalDateTime.now());
        testBooking.setEnd(LocalDateTime.now());
        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(mockBookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class))
                )
                .thenReturn(List.of(testBooking));

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("good sword. Al Mualim liked it.");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(any(Comment.class)))
                .thenReturn(testComment);

        CommentDto testCommentDto = CommentMapper.toCommentDto(testComment);

        CommentDto addedCommentDto = itemService.addNewCommentForItem(1L, 1L, testCommentDto);

        Assertions.assertEquals(testCommentDto, addedCommentDto);
        Assertions.assertEquals(testCommentDto.getText(), "good sword. Al Mualim liked it.");
        Assertions.assertEquals(testCommentDto.getAuthorName(), "Altair");
    }

    @Test
    void addNewCommentForItemByNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("good sword. Al Mualim liked it.");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now());

        CommentDto testCommentDto = CommentMapper.toCommentDto(testComment);

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemService.addNewCommentForItem(1L, 1L, testCommentDto)
        );

        Assertions.assertEquals("Пользователь с id = 1 не найден.", error.getMessage());
    }

    @Test
    void addNewCommentForNotExistingItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("good sword. Al Mualim liked it.");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now());

        CommentDto testCommentDto = CommentMapper.toCommentDto(testComment);

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> itemService.addNewCommentForItem(1L, 1L, testCommentDto)
        );

        Assertions.assertEquals("Вещь с id = 1 не найдена.", error.getMessage());
    }

    @Test
    void addNewCommentForItemByNotEligibleUser() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(mockBookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class))
                )
                .thenReturn(List.of());

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("good sword. Al Mualim liked it.");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now());
        Mockito
                .when(mockCommentRepository.save(any(Comment.class)))
                .thenReturn(testComment);

        CommentDto testCommentDto = CommentMapper.toCommentDto(testComment);

        BookingBadRequest error = Assertions.assertThrows(
                BookingBadRequest.class, () -> itemService.addNewCommentForItem(1L, 1L, testCommentDto)
        );

        Assertions.assertEquals("Пользователь с id = 1 не арендовывал вещь с id = 1.", error.getMessage());
    }
}
