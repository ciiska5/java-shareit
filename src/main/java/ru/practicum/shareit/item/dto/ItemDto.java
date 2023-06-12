package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
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
}
