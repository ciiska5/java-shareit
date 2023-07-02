package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDateDto {

    private Long id; //уникальный идентификатор вещи

    @NotBlank
    private String name; //краткое название

    @NotNull
    private String description; //развёрнутое описание

    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды

    private ItemRequestDto request; //если вещь была создана по запросу другого пользователя, то в этом
                                    //поле будет храниться ссылка на соответствующий запрос.

    private BookingDateDto lastBooking; //последнее бронирование

    private BookingDateDto nextBooking; //ближайшее следующее бронирование

    private List<CommentDto> comments; // список отзывов

    public ItemDateDto(Long id, String name, String description, Boolean available, ItemRequestDto request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
