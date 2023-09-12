package ru.practicum.shareit.item.comment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//уникальный идентификатор комментария

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
