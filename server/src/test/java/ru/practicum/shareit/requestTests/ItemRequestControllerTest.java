package ru.practicum.shareit.requestTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//тесты для REST-эндпоинтов класса ItemRequestController

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_KEY = "X-Sharer-User-Id";
    private final ItemRequestDto testItemRequestDto = new ItemRequestDto();

    @BeforeEach
    void initTestItemRequestDto() {
        testItemRequestDto.setId(1L);
        testItemRequestDto.setDescription("Eden's Apple");
        testItemRequestDto.setItems(List.of());
        testItemRequestDto.setCreated(LocalDateTime.now());
    }

    @Test
    void addNewRequestTest() throws Exception {
        Mockito
                .when(itemRequestService.addNewRequest(anyLong(),any()))
                .thenReturn(testItemRequestDto);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(testItemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.items", is(testItemRequestDto.getItems()), List.class));

        verify(itemRequestService, times(1)).addNewRequest(anyLong(),any());
    }

    @Test
    void getAllByRequestorTest() throws Exception {
        Mockito
                .when(itemRequestService.getAllByRequestor(anyLong()))
                .thenReturn(List.of(testItemRequestDto));

        mvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(testItemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items", is(testItemRequestDto.getItems()), List.class));

        verify(itemRequestService, times(1)).getAllByRequestor(anyLong());
    }

    @Test
    void getAllItemRequestsTest() throws Exception {
        Mockito
                .when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemRequestDto));

        mvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(testItemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items", is(testItemRequestDto.getItems()), List.class));

        verify(itemRequestService, times(1)).getAllItemRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getItemRequestByIdTest() throws Exception {
        Mockito
                .when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(testItemRequestDto);

        mvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(testItemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.items", is(testItemRequestDto.getItems()), List.class));

        verify(itemRequestService, times(1)).getItemRequestById(anyLong(), anyLong());
    }
}
