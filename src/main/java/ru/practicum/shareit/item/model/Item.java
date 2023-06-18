package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;

/**
 * Шаблон объекта Item для хранилища
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id; //уникальный идентификатор вещи
    @NotBlank
    private String name; //краткое название
    @NotNull
    private String description; //развёрнутое описание
    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    private User user; // владелец вещи
    private ItemRequest request; //если вещь была создана по запросу другого пользователя, то в этом
                                 //поле будет храниться ссылка на соответствующий запрос.

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

}
