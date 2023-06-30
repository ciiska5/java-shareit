package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    private long id; //уникальный идентификатор запроса
    private String description; //текст запроса, содержащий описание требуемой вещи
    private User requestor; // пользователь, создавший запрос
    private Date created;// дата и время создания
}
