package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Шаблон объекта Item для хранилища
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор вещи

    @NotBlank
    @Column(nullable = false)
    private String name; //краткое название

    @NotBlank
    @Column(length = 3000, nullable = false)
    private String description; //развёрнутое описание

    @NotNull
    private Boolean available; // статус о том, доступна или нет вещь для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // владелец вещи

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request; //если вещь была создана по запросу другого пользователя, то в этом
                                 //поле будет храниться ссылка на соответствующий запрос.

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
