package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        log.info("Добавлен новый пользователь с id = {}. ", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        log.info("Пользователь с id = {} получен. ", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> usersDto.add(UserMapper.toUserDto(user)));
        log.info("Получены все пользователи.");
        return usersDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, Long userId) {
        User updatingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        String newEmail = userDto.getEmail();
        String newName = userDto.getName();
        if (newEmail != null) {
            updatingUser.setEmail(newEmail);
        }
        if (newName != null) {
            updatingUser.setName(newName);
        }
        log.info("Пользователь с id = {} обновлен. ", userId);
        return UserMapper.toUserDto(userRepository.save(updatingUser));
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        log.info("Пользователь с id = {} удален. ", userId);
    }
}
