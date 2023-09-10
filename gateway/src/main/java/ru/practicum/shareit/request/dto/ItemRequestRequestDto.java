package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаблон объекта ItemRequest для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRequestDto {
    private Long id; //уникальный идентификатор запроса

    @NotNull
    private String description; //текст запроса, содержащий описание требуемой вещи

    private List<ItemRequestDto> items; // список ответов на запрос вещи

    private LocalDateTime created;// дата и время создания
}
