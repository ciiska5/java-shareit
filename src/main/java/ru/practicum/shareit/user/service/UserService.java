package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addNewUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User updateUser(User user, Long userId);

    void deleteUserById(Long userId);
}
