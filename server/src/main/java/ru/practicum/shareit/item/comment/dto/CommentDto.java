package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;//уникальный идентификатор комментария

    private String text;//содержимое комментария

    private String authorName; // автор комментария

    private LocalDateTime created; //дата создания комментария.
}
