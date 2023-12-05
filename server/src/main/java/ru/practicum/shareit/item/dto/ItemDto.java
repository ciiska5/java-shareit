package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;

/**
 * Шаблон объекта Item для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id; //уникальный идентификатор вещи

    private String name; //краткое название

    private String description; //развёрнутое описание

    private Boolean available; // статус о том, доступна или нет вещь для аренды

    private Long requestId; //если вещь была создана по запросу другого пользователя, то в этом
                                 //поле будет храниться ссылка на соответствующий запрос.

    private List<CommentDto> comments; // список отзывов
}
