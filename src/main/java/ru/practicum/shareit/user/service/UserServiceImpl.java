package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserDAO userDAO;

    @Override
    public User addNewUser(User user) {
       return userDAO.addNewUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User updateUser(User user, Long userId) {
        return userDAO.updateUser(user, userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        userDAO.deleteUserById(userId);
    }
}
