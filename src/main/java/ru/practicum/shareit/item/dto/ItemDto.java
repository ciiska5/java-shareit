package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Шаблон объекта Item для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id; //уникальный идентификатор вещи

    @NotBlank
    private String name; //краткое название

    @NotNull
    private String description; //развёрнутое описание

    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды

    private ItemRequestDto request; //если вещь была создана по запросу другого пользователя, то в этом
                                 //поле будет храниться ссылка на соответствующий запрос.

    private List<CommentDto> comments; // список отзывов
}
