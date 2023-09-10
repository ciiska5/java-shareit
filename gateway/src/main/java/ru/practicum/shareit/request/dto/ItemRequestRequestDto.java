package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Шаблон объекта ItemRequest для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRequestDto {

    @NotNull
    private String description; //текст запроса, содержащий описание требуемой вещи
}
