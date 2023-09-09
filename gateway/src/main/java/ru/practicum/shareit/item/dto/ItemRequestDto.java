package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id; //уникальный идентификатор вещи

    @NotBlank
    private String name; //краткое название

    @NotNull
    private String description; //развёрнутое описание

    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды

    private Long requestId; //если вещь была создана по запросу другого пользователя, то в этом
                                 //поле будет храниться ссылка на соответствующий запрос.
}
