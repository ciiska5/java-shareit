package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookItemRequestDto {
    private Long id; // уникальный идентификатор бронирования

    @NotNull
    @FutureOrPresent
    private LocalDateTime start; // дата и время начала бронирования

    @NotNull
    @Future
    private LocalDateTime end; // дата и время конца бронирования
}
