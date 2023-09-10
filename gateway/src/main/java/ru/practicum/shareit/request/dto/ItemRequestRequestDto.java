package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаблон объекта ItemRequest для пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRequestDto {

    @NotBlank
    private String description; //текст запроса, содержащий описание требуемой вещи
}
