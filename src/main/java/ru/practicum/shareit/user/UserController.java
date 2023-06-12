package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //добавление нового пользователя
    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        return userService.addNewUser(user);
    }

    //получение пользователя по id
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    //получение всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //обновление пользователя
    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable("userId") Long userId) {
        return userService.updateUser(user, userId);
    }

    //удаление пользователя по id
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
