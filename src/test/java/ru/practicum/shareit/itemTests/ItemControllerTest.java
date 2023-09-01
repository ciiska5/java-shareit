package ru.practicum.shareit.itemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//тесты для REST-эндпоинтов класса ItemController

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mvc;

    private final String USER_KEY = "X-Sharer-User-Id";
    private final ItemDto testItemDto = new ItemDto();
    private final ItemDateDto testItemDateDto = new ItemDateDto();

    @BeforeEach
    void initTestItemDto() {
        testItemDto.setId(1L);
        testItemDto.setName("Eden's Apple");
        testItemDto.setDescription("Real Apple of Eden");
        testItemDto.setRequestId(1L);
        testItemDto.setAvailable(Boolean.TRUE);

        testItemDateDto.setId(1L);
        testItemDateDto.setName("Eden's Apple");
        testItemDateDto.setDescription("Real Apple of Eden");
        testItemDateDto.setRequestId(1L);
        testItemDateDto.setAvailable(Boolean.TRUE);
        testItemDateDto.setLastBooking(null);
        testItemDateDto.setNextBooking(null);
        testItemDateDto.setComments(List.of());
    }

    @Test
    void addNewItemTest() throws Exception {
        Mockito
                .when(itemService.addNewItem(any(), anyLong()))
                .thenReturn(testItemDto);

        mvc.perform(post("/items")
                    .content(objectMapper.writeValueAsString(testItemDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .header(USER_KEY, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(testItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(testItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(testItemDto.getRequestId()), Long.class));
    }

    @Test
    void updateItemTest() throws Exception {
        testItemDto.setDescription("Fake Eden's Apple in fact");
        Mockito
                .when(itemService.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(testItemDto);

        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(testItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(testItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(testItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(testItemDto.getRequestId()), Long.class));
    }

    @Test
    void getItemByIdTest() throws Exception {
        Mockito
                .when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(testItemDateDto);

        mvc.perform(get("/items/1")
                        .content(objectMapper.writeValueAsString(testItemDateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemDateDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemDateDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(testItemDateDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(testItemDateDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(testItemDateDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking", is(testItemDateDto.getLastBooking()), BookingDateDto.class))
                .andExpect(jsonPath("$.nextBooking", is(testItemDateDto.getNextBooking()), BookingDateDto.class))
                .andExpect(jsonPath("$.comments", is(testItemDateDto.getComments()), List.class));
    }

    @Test
    void getAllItemsOfUserTest() throws Exception {
        Mockito
                .when(itemService.getAllItemsOfUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemDateDto));

        mvc.perform(get("/items")
                        .content(objectMapper.writeValueAsString(testItemDateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testItemDateDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testItemDateDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(testItemDateDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(testItemDateDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].requestId", is(testItemDateDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking", is(testItemDateDto.getLastBooking()), BookingDateDto.class))
                .andExpect(jsonPath("$[0].nextBooking", is(testItemDateDto.getNextBooking()), BookingDateDto.class))
                .andExpect(jsonPath("$[0].comments", is(testItemDateDto.getComments()), List.class));
    }

    @Test
    void getItemsByRequestTextTest() throws Exception {
        Mockito
                .when(itemService.getItemsByRequestText(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemDto));

        mvc.perform(get("/items/search")
                        .content(objectMapper.writeValueAsString(testItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .queryParam("text", "AppLE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testItemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testItemDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(testItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(testItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].requestId", is(testItemDto.getRequestId()), Long.class));
    }

    @Test
    void addNewCommentForItemTest() throws Exception {
        CommentDto testCommentDto = new CommentDto();
        testCommentDto.setId(1L);
        testCommentDto.setText("It's fake apple. Do not recommend.");
        testCommentDto.setAuthorName("Al Mualim");
        testCommentDto.setCreated(LocalDateTime.now());

        Mockito
                .when(itemService.addNewCommentForItem(anyLong(), anyLong(), any()))
                .thenReturn(testCommentDto);

        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_KEY, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(testCommentDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(testCommentDto.getAuthorName()), String.class));
    }
}
