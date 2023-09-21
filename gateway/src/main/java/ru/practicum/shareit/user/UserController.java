package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

/**
 * Класс-контроллер сущности User
 */
@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    //добавление нового пользователя
    @PostMapping
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserRequestDto requestDto) {
        log.info("Creating user");
        return userClient.addNewUser(requestDto);
    }

    //получение пользователя по id
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        log.info("Get user {}", userId);
        return userClient.getUserById(userId);
    }

    //получение всех пользователей
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    //обновление пользователя
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto requestDto, @PathVariable("userId") Long userId) {
        log.info("Update user {}", userId);
        return userClient.updateUser(requestDto, userId);
    }

    //удаление пользователя по id
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") Long userId) {
        log.info("Delete user {}", userId);
        return userClient.deleteUserById(userId);
    }
}
