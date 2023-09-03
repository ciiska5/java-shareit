package ru.practicum.shareit.userTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//тесты для REST-эндпоинтов класса UserController

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private final UserDto testUserDto = new UserDto();

    @BeforeEach
    void initTestUser() {
        testUserDto.setId(1L);
        testUserDto.setName("Altair");
        testUserDto.setEmail("matterLabs@gmail.com");
    }

    @Test
    void addUserTest() throws Exception {
        Mockito
                .when(userService.addNewUser(any()))
                .thenReturn(testUserDto);

        mvc.perform(post("/users")
                    .content(objectMapper.writeValueAsString(testUserDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail()), String.class));

        verify(userService, times(1)).addNewUser(any());
    }

    @Test
    void getUserByIdTest() throws Exception {
        Mockito
                .when(userService.getUserById(anyLong()))
                .thenReturn(testUserDto);

        mvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail()), String.class));

        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    void updateUserTest() throws Exception {
        testUserDto.setEmail("mltt@gmail.com");
        Mockito
                .when(userService.updateUser(any(), anyLong()))
                .thenReturn(testUserDto);

        mvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail()), String.class));

        verify(userService, times(1)).updateUser(any(), anyLong());
    }

    @Test
    void getAllUsersTest() throws Exception {
        Mockito
                .when(userService.getAllUsers())
                .thenReturn(List.of(testUserDto));

        mvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testUserDto.getName()), String.class))
                .andExpect(jsonPath("$[0].email", is(testUserDto.getEmail()), String.class));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        Mockito
                .doNothing()
                .when(userService)
                .deleteUserById(anyLong());

        mvc.perform(delete("/users/1")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(anyLong());
    }

}
