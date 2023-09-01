package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


public class UserServiceTest {
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);

    private UserServiceImpl userService;
    private UserDto testUserDto;

    @BeforeEach
    void initUserDto() {
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("Altair");
        testUserDto.setEmail("matterLabs@gmail.com");
    }

    @BeforeEach
    void initUserService() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void addUserTest() {
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(UserMapper.toUser(testUserDto));

        UserDto savedUser = userService.addNewUser(testUserDto);

        Assertions.assertEquals(savedUser, testUserDto);
    }

    @Test
    void getUserByCorrectIdTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(testUserDto)));

        UserDto savedUser = userService.getUserById(1L);

        Assertions.assertEquals(savedUser, testUserDto);
    }

    @Test
    void getUserByIncorrectIdTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException error = Assertions
                .assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));

        Assertions.assertEquals("Пользователь с id 2 не найден", error.getMessage());
    }

    @Test
    void updateUsersEmailTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(testUserDto)));

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setEmail("zkSync@gmail.com");

        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(UserMapper.toUser(updatedUserDto));

        UserDto savedUpdatedUserDto = userService.updateUser(updatedUserDto,1L);

        Assertions.assertEquals("zkSync@gmail.com", savedUpdatedUserDto.getEmail());
    }

    @Test
    void updateUsersNameTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(testUserDto)));

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Ezio");

        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(UserMapper.toUser(updatedUserDto));

        UserDto savedUpdatedUserDto = userService.updateUser(updatedUserDto,1L);

        Assertions.assertEquals("Ezio", savedUpdatedUserDto.getName());
    }
}
