package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserDAOImpl implements UserDAO {

    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>(); //сет для хранения уникальных e-mail

    @Override
    public void addNewUser(User user) {
        String email = user.getEmail();
        checkDuplicateEmail(email);
        user.setId(getId());
        log.info("Добавлен новый пользователь с id = {}. ", user.getId());
        users.put(user.getId(), user);
    }

    @Override
    public User getUserById(Long userId) {
        User user = checkUsersExistenceById(userId);
        log.info("Пользователь с id = {} получен. ", userId);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получены все пользователи.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user, Long userId) {
        User updatingUser = getUserById(userId);
        String oldEmail = updatingUser.getEmail();
        String newEmail = user.getEmail();
        String newName = user.getName();
        if (newEmail != null && !newEmail.equals(oldEmail)) {
            checkDuplicateEmail(newEmail);
            emails.remove(oldEmail);
            updatingUser.setEmail(newEmail);
        }
        if (newName != null) {
            updatingUser.setName(newName);
        }
        log.info("Пользователь с id = {} обновлен. ", userId);
        return updatingUser;
    }

    @Override
    public void deleteUserById(Long userId) {
        checkUsersExistenceById(userId);
        emails.remove(getUserById(userId).getEmail());
        log.info("Пользователь с id = {} удален. ", userId);
        users.remove(userId);
    }

    //получение id для пользователя
    private long lastId = 1;

    private Long getId() {
        return lastId++;
    }

    //проверка e-mail на повтор
    private void checkDuplicateEmail(String email) {
        if (!emails.add(email)) {
            log.error("Введенный e-mail занят другим пользователем");
            throw new EmailExistsException("Введенный e-mail занят другим пользователем");
        }
    }

    //проверка пользователя на существование
    private User checkUsersExistenceById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            log.error("Пользователь с id = {} не найден. ", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user;
    }
}
