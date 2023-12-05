package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаблон объекта ItemRequest для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id; //уникальный идентификатор запроса

    private String description; //текст запроса, содержащий описание требуемой вещи

    private List<ItemDto> items; // список ответов на запрос вещи

    private LocalDateTime created;// дата и время создания
}
