package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    //добавление нового запроса на бронирование
    BookingDto createNewBooking(BookingDateDto bookingDateDto, Long userId);

    //Подтверждение или отклонение запроса на бронирование (только владелцем вещи)
    BookingDto approveRequest(Long bookingId, Long userId, Boolean approved);

    //Получение данных о конкретном бронировании (включая его статус) либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
    BookingDto getById(Long bookingId, Long userId);

    //Получение списка всех бронирований текущего пользователя (арендодатора)
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    List<BookingDto> getAllBookingsOfUser(Long userId, String state, int from, int size);

    //Получение списка всех бронирований текущего пользователя (арендодателя)
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    List<BookingDto> getAllBookedItemsOfUser(Long userId, String state, int from, int size);
}
