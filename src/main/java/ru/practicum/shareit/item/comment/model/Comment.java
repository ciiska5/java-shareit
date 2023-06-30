package ru.practicum.shareit.item.comment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//уникальный идентификатор комментария

    @NotBlank
    @Column(length = 3000, nullable = false)
    private String text;//содержимое комментария

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;//вещь, к которой относится комментарий

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // автор комментария

    private LocalDateTime created; //дата создания комментария.
}
