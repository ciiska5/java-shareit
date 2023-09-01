package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServImpl;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class BookingServiceTest {

    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);

    private final BookingServImpl bookingServ = new BookingServImpl(
            mockBookingRepository, mockUserRepository, mockItemRepository);

    private Booking testBooking;
    private User testUser;
    private User testItemOwner;
    private Item testItem;

    @BeforeEach
    void initTestBooking() {
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setStart(LocalDateTime.now());
        testBooking.setEnd(LocalDateTime.now().plusMinutes(5));
    }

    @BeforeEach
    void initTestUser() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Altair");
        testUser.setEmail("matterLabs@gmail.com");
    }

    @BeforeEach
    void initTestItemOwner() {
        testItemOwner = new User();
        testItemOwner.setId(2L);
        testItemOwner.setName("Ezio");
        testItemOwner.setEmail("blacksmith@gmail.com");
    }

    @BeforeEach
    void initTestItem() {
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Sword");
        testItem.setDescription("Sharp sword for enemies");
        testItem.setAvailable(Boolean.TRUE);
        testItem.setRequest(new ItemRequest());
    }

    @Test
    void createNewBookingTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(mockBookingRepository.save(any(Booking.class)))
                .thenReturn(testBooking);

        testItem.setOwner(testItemOwner);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);
        BookingDto testBookingDto = BookingMapper.toBookingDto(testBooking);
        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);
        System.out.println(testBookingDateDto);

        BookingDto createdBookingDto = bookingServ.createNewBooking(testBookingDateDto, 1L);

        Assertions.assertEquals(createdBookingDto, testBookingDto);
    }

    @Test
    void createNewBookingByNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        testItem.setOwner(testItemOwner);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);

        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> bookingServ.createNewBooking(testBookingDateDto, 1L)
        );

        Assertions.assertEquals("Пользователь с id = 1 не найден.", error.getMessage());
    }

    @Test
    void createNewBookingOfNotExistingItemTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        testItem.setOwner(testItemOwner);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);

        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> bookingServ.createNewBooking(testBookingDateDto, 1L)
        );

        Assertions.assertEquals("Вещь с id = 1 не найдена.", error.getMessage());
    }

    @Test
    void createNewBookingByItemOwnerTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));

        testItem.setOwner(testUser);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);

        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);

        ItemNotFoundException error = Assertions.assertThrows(
                ItemNotFoundException.class, () -> bookingServ.createNewBooking(testBookingDateDto, 1L)
        );

        Assertions.assertEquals(
                "Пользователь с id = 1 не может забронировать собственную вещь.", error.getMessage()
        );
    }

    @Test
    void createNewBookingOfNotAvailableTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));

        testItem.setOwner(testItemOwner);
        testItem.setAvailable(Boolean.FALSE);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);

        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);

        ItemAlreadyBookedException error = Assertions.assertThrows(
                ItemAlreadyBookedException.class, () -> bookingServ.createNewBooking(testBookingDateDto, 1L)
        );

        Assertions.assertEquals("Вещь с id = 1 недоступна для бронирования.", error.getMessage());
    }

    @Test
    void createNewBookingWithIncorrectBookingEndTimeTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));

        testItem.setOwner(testItemOwner);

        testBooking.setBooker(testUser);
        testBooking.setItem(testItem);
        testBooking.setEnd(testBooking.getEnd().minusMinutes(10));

        BookingDateDto testBookingDateDto = BookingMapper.toBookingDateDto(testBooking);

        BookingDateException error = Assertions.assertThrows(
                BookingDateException.class, () -> bookingServ.createNewBooking(testBookingDateDto, 1L)
        );

        Assertions.assertEquals(
                "Дата окончания бронирования не должна быть раньше даты начала бронирования.",
                error.getMessage()
        );
    }

    @Test
    void approveRequestApprovedTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        BookingDto testBookingDto = BookingMapper.toBookingDto(testBooking);
        testBookingDto.setStatus(BookingStatus.APPROVED);

        BookingDto approvedBookingDto = bookingServ
                .approveRequest(testBooking.getId(), testItemOwner.getId(), true);

        Assertions.assertEquals(approvedBookingDto, testBookingDto);
    }

    @Test
    void approveRequestRejectedTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        BookingDto testBookingDto = BookingMapper.toBookingDto(testBooking);
        testBookingDto.setStatus(BookingStatus.REJECTED);

        BookingDto approvedBookingDto = bookingServ
                .approveRequest(testBooking.getId(), testItemOwner.getId(), false);

        Assertions.assertEquals(approvedBookingDto, testBookingDto);
    }

    @Test
    void approveRequestOfNotExistingBookingTest() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        BookingNotFoundException error = Assertions.assertThrows(
                BookingNotFoundException.class, () -> bookingServ.approveRequest(1L, 2L, true)
        );

        Assertions.assertEquals("Бронирование с id = 1 не найдено.", error.getMessage());
    }

    @Test
    void approveRequestNotBelongToOwnerTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        BookingNotFoundException error = Assertions.assertThrows(
                BookingNotFoundException.class, () -> bookingServ.approveRequest(1L, 1L, true)
        );

        Assertions.assertEquals(
                "У пользователя с id = 1 не найден запрос на бронирование с id = 1.", error.getMessage()
        );
    }

    @Test
    void approveRequestWithoutWaitingStatusTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.REJECTED);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        ItemAlreadyBookedException error = Assertions.assertThrows(
                ItemAlreadyBookedException.class, () -> bookingServ.approveRequest(1L, 2L, true)
        );

        Assertions.assertEquals("Запрос на бронирование уже подтвержден или отклонен.", error.getMessage());
    }

    @Test
    void getByIdTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        BookingDto testBookingDto = BookingMapper.toBookingDto(testBooking);

        BookingDto foundBookingDto = bookingServ.getById(testBooking.getId(), testItemOwner.getId());

        Assertions.assertEquals(testBookingDto, foundBookingDto);
    }

    @Test
    void getByIdNotExistingBookingTest() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        BookingNotFoundException error = Assertions.assertThrows(
                BookingNotFoundException.class, () -> bookingServ.getById(1L, 2L)
        );

        Assertions.assertEquals("Бронирование с id = 1 не найдено.", error.getMessage());
    }

    @Test
    void getByIdByNotOwnerOrNotBookerTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));

        UserNotFoundException errorOwner = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingServ.getById(testBooking.getId(), testItemOwner.getId() + 10)
        );
        UserNotFoundException errorBooker = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingServ.getById(testBooking.getId(), testUser.getId() + 10)
        );

        Assertions.assertEquals(
                "Пользователь c id = 12 не является арендотатором или владельцем вещи.",
                errorOwner.getMessage()
        );
        Assertions.assertEquals(
                "Пользователь c id = 11 не является арендотатором или владельцем вещи.",
                errorBooker.getMessage()
        );
    }

    @Test
    void getAllBookingsOfUserTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockBookingRepository.findAllByBooker(any(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking)));

        List<BookingDto> foundBookingDtoList = bookingServ.getAllBookingsOfUser(1L, "ALL", 0, 1);

        Assertions.assertEquals(1, foundBookingDtoList.size());
        BookingDto foundBookingDto = foundBookingDtoList.get(0);
        Assertions.assertEquals(testBooking.getId(), foundBookingDto.getId());
        Assertions.assertEquals(testBooking.getBooker().getId(), foundBookingDto.getBooker().getId());
    }

    @Test
    void getAllBookedItemsOfUserTest() {
        testItem.setOwner(testItemOwner);

        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockBookingRepository.findAllByItemOwner(any(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking)));

        List<BookingDto> foundBookedItemsDtoList = bookingServ.getAllBookedItemsOfUser(1L, "ALL", 0, 1);

        Assertions.assertEquals(1, foundBookedItemsDtoList.size());
        BookingDto foundBookingDto = foundBookedItemsDtoList.get(0);
        Assertions.assertEquals(testBooking.getId(), foundBookingDto.getId());
        Assertions.assertEquals(testBooking.getBooker().getId(), foundBookingDto.getBooker().getId());
    }
}
