package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Шаблон объекта User для хранилища
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор пользователя

    @NotBlank
    @Column(nullable = false)
    private String name; // имя или логин пользователя;

    @NotEmpty
    @Email(message = "email не соответствует нужному формату",
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @Column(length = 512, nullable = false, unique = true)
    private String email; // адрес электронной почты (два пользователя не могут иметь одинаковый email)

}
