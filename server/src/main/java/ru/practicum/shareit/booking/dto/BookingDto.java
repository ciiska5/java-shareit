package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * Шаблон объекта Booking для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id; // уникальный идентификатор бронирования

    private LocalDateTime start; // дата и время начала бронирования

    private LocalDateTime end; // дата и время конца бронирования

    private ItemDateDto item;// вещь, которую пользователь бронирует

    private UserDto booker; // пользователь, который осуществляет бронирование;

    private BookingStatus status; // статус бронирования
}
