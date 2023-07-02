package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id; //уникальный идентификатор запроса

    @NotBlank
    private String description; //текст запроса, содержащий описание требуемой вещи

    private UserDto requestor; // пользователь, создавший запрос

    private LocalDateTime created;// дата и время создания
}
