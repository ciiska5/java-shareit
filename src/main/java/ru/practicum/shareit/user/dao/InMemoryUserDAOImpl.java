package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class InMemoryUserDAOImpl implements UserDAO{

    private final List<User> users = new ArrayList<>();
    @Override
    public User addNewUser(User user) {
        checkDuplicateEmail(user.getEmail());
        user.setId(getId());
        users.add(user);
        log.info("Добавлен новый пользователь с id = {}. " , user.getId());
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        checkUsersExistenceById(userId);
        log.info("Пользователь с id = {} получен. ", userId);
        return users.stream().
                filter(user -> Objects.equals(user.getId(), userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получены все пользователи.");
        return users;
    }

    @Override
    public User updateUser(User user, Long userId) {
        User updatingUser = getUserById(userId);
        String newEmail = user.getEmail();
        String newName = user.getName();
        if(newEmail != null) {
            if(!newEmail.equals(updatingUser.getEmail())) {
                checkDuplicateEmail(newEmail);
            }
            updatingUser.setEmail(newEmail);
        }
        if(newName != null) {
            updatingUser.setName(newName);
        }
        log.info("Пользователь с id = {} обновлен. ", userId);
        return updatingUser;
    }

    @Override
    public void deleteUserById(Long userId) {
        checkUsersExistenceById(userId);
        log.info("Пользователь с id = {} удален. ", userId);
        users.remove(users.stream().
                filter(user -> Objects.equals(user.getId(), userId))
                .findFirst()
                .orElseThrow());
    }

    //получение id для пользователя
    private long lastId = 1;
    private Long getId() {
        return lastId++;
    }

    //проверка e-mail на повтор
    private void checkDuplicateEmail(String email) {
        if(users.stream()
                .anyMatch(user -> user.getEmail().equals(email))) {
            log.error("Введенный e-mail занят другим пользователем");
            throw new EmailExistsException("Введенный e-mail занят другим пользователем");
        }
    }

    //проверка пользователя на существование
    private void checkUsersExistenceById(Long userId) {
        if(users.stream()
                .noneMatch(user -> Objects.equals(user.getId(), userId))){
            log.error("Пользователь с id = {} не найден. ", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
    }
}
