package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDAO {

    void addNewUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User updateUser(User user, Long userId);

    void deleteUserById(Long userId);
}
