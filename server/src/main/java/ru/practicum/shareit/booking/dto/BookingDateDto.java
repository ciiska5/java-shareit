package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDateDto {

    private Long id; // уникальный идентификатор бронирования

    private LocalDateTime start; // дата и время начала бронирования

    private LocalDateTime end; // дата и время конца бронирования

    private Long itemId;// вещь, которую пользователь бронирует

    private Long bookerId; // пользователь, который осуществляет бронирование;
}
