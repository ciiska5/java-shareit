package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
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
public class BookingServImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private static final Sort sort = Sort.by(Sort.Direction.DESC, "start"); //параметр для сортировки бронирований от старых к новым

    @Override
    @Transactional
    public BookingDto createNewBooking(BookingDateDto bookingDateDto, Long userId) {
        User user = checkUsersExistenceById(userId);
        Item item = checkItemsExistenceById(bookingDateDto.getItemId());

        if (item.getOwner().getId().equals(userId)) {
            log.error("Пользователь с id = {} не может забронировать собственную вещь.", userId);
            throw new ItemNotFoundException("Пользователь с id = " + userId
                    + " не может забронировать собственную вещь.");
        }
        if (!item.getAvailable()) {
            log.error("Вещь с id = {} недоступна для бронирования.", item.getId());
            throw new ItemAlreadyBookedException("Вещь с id = " + item.getId() + " недоступна для бронирования.");
        }
        Booking booking = BookingMapper.toBooking(bookingDateDto);
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().equals(booking.getStart())) {
            log.error("Дата окончания бронирования не должна быть раньше даты начала бронирования.");
            throw new BookingDateException("Дата окончания бронирования не должна " +
                    "быть раньше даты начала бронирования.");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        log.info("Пользователь с id = {} забронировал вещь с id = {}. ", userId, item.getId());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveRequest(Long bookingId, Long userId, Boolean approved) {
        Booking booking = checkBookingExistenceById(bookingId);

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            log.error("У пользователя с id = {} не найден запрос на бронирование с id = {}. ", userId, bookingId);
            throw new BookingNotFoundException(
                    "У пользователя с id = " + userId + " не найден запрос на бронирование с id = " + bookingId + ".");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            log.error("Запрос на бронирование уже подтвержден или отклонен.");
            throw new ItemAlreadyBookedException("Запрос на бронирование уже подтвержден или отклонен.");
        }
        if (approved) {
            log.info("Пользователь с id = {} подтвердил бронирование.", userId);
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            log.info("Пользователь с id = {} отклонил бронирование.", userId);
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = checkBookingExistenceById(bookingId);
        Long bookingOwnerId = booking.getItem().getOwner().getId();
        Long bookingBookerId = booking.getBooker().getId();

        if (!userId.equals(bookingOwnerId) && !userId.equals(bookingBookerId)) {
            log.error("Пользователь c id = {} не является арендотатором или владельцем вещи. ", userId);
            throw new UserNotFoundException(
                    "Пользователь c id = " + userId + " не является арендотатором или владельцем вещи."
            );
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsOfUser(Long userId, String state, int from, int size) {
        User booker = checkUsersExistenceById(userId);

        List<Booking> allBookingsOfUserList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        switch (state) {
            case "ALL":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBooker(booker, pageable).toList());
                break;
            case "CURRENT":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(
                        booker, LocalDateTime.now(), LocalDateTime.now(), pageable).toList());
                break;
            case "PAST":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBookerAndEndBefore(
                        booker, LocalDateTime.now(), pageable).toList());
                break;
            case "FUTURE":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBookerAndStartAfter(
                        booker, LocalDateTime.now(), pageable).toList());
                break;
            case "WAITING":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBookerAndStatusEquals(
                        booker, BookingStatus.WAITING, pageable).toList());
                break;
            case "REJECTED":
                allBookingsOfUserList.addAll(bookingRepository.findAllByBookerAndStatusEquals(
                        booker, BookingStatus.REJECTED, pageable).toList());
                break;
            default:
                log.error("Неизвестный статус с параметром {}.", state);
                throw new UnknownBookingStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("Получен список всех бронирований пользователя с id = {} со статусом {}", userId, state);
        return allBookingsOfUserList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookedItemsOfUser(Long userId, String state, int from, int size) {
        User owner = checkUsersExistenceById(userId);

        List<Booking> allBookedItemsOfOwnerList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        switch (state) {
            case "ALL":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwner(owner, pageable).toList());
                break;
            case "CURRENT":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(
                        owner, LocalDateTime.now(), LocalDateTime.now(), pageable).toList());
                break;
            case "PAST":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwnerAndEndBefore(
                        owner, LocalDateTime.now(), pageable).toList());
                break;
            case "FUTURE":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwnerAndStartAfter(
                        owner, LocalDateTime.now(), pageable).toList());
                break;
            case "WAITING":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(
                        owner, BookingStatus.WAITING, pageable).toList());
                break;
            case "REJECTED":
                allBookedItemsOfOwnerList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(
                        owner, BookingStatus.REJECTED, pageable).toList());
                break;
            default:
                log.error("Неизвестный статус с параметром {}.", state);
                throw new UnknownBookingStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("Получен список бронирований для всех вещей пользователя с id = {} со статусом {}", userId, state);
        return allBookedItemsOfOwnerList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    //проверка пользователя на существование
    private User checkUsersExistenceById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден."));
    }

    //проверка вещи на существование
    private Item checkItemsExistenceById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id = " + itemId + " не найдена."));
    }

    //проверка бронирования на существование
    private Booking checkBookingExistenceById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id = " + bookingId + " не найдено."));
    }
}
