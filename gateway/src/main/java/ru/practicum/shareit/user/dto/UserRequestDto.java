package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotNull
    @NotBlank
    private String name; // имя или логин пользователя;
    @NotNull
    @NotEmpty
    @Email(message = "email не соответствует нужному формату",
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email; // адрес электронной почты (два пользователя не могут иметь одинаковый email)

}
