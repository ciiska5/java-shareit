package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * Шаблон объекта User для хранилища
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор пользователя

    @Column(nullable = false)
    private String name; // имя или логин пользователя;

    @Column(length = 512, nullable = false, unique = true)
    private String email; // адрес электронной почты (два пользователя не могут иметь одинаковый email)
}