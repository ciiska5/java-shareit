package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Шаблон объекта ItemRequest для хранилища
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор запроса

    @NotBlank
    @Column(length = 3000, nullable = false)
    private String description; //текст запроса, содержащий описание требуемой вещи

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor; // пользователь, создавший запрос

    private Date created;// дата и время создания
}
