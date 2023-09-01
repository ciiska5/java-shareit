package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//тесты для REST-эндпоинтов класса ItemController

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    private final String USERKEY = "X-Sharer-User-Id";
    private final BookingDto testBookingDto = new BookingDto();

    @BeforeEach
    void initTestBookingDto() {
        testBookingDto.setId(1L);
        testBookingDto.setStart(LocalDateTime.now().plusMinutes(10));
        testBookingDto.setEnd(LocalDateTime.now().plusMinutes(30));
        testBookingDto.setItem(new ItemDateDto(1L, "Eden's Apple", "Real Eden's Apple", true, 1L));
        testBookingDto.setBooker(new UserDto(2L, "Al Mualim", "ubisoft@gmail.com"));
        testBookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createNewBookingTest() throws Exception {
        Mockito
                .when(bookingService.createNewBooking(any(), anyLong()))
                .thenReturn(testBookingDto);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USERKEY, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item", is(testBookingDto.getItem()), ItemDateDto.class))
                .andExpect(jsonPath("$.booker", is(testBookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(testBookingDto.getStatus().toString()), String.class));
    }

    @Test
    void approveRequestTest() throws Exception {
        testBookingDto.setStatus(BookingStatus.APPROVED);

        Mockito
                .when(bookingService.approveRequest(anyLong(), anyLong(), any()))
                .thenReturn(testBookingDto);

        mvc.perform(patch("/bookings/1")
                        .content(objectMapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USERKEY, 2L)
                        .queryParam("approved", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item", is(testBookingDto.getItem()), ItemDateDto.class))
                .andExpect(jsonPath("$.booker", is(testBookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(testBookingDto.getStatus().toString()), String.class));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        Mockito
                .when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(testBookingDto);

        mvc.perform(get("/bookings/1")
                        .content(objectMapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USERKEY, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item", is(testBookingDto.getItem()), ItemDateDto.class))
                .andExpect(jsonPath("$.booker", is(testBookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(testBookingDto.getStatus().toString()), String.class));
    }

    @Test
    void getAllBookingsOfUserTest() throws Exception {
        Mockito
                .when(bookingService.getAllBookingsOfUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(testBookingDto));

        mvc.perform(get("/bookings")
                        .content(objectMapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USERKEY, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item", is(testBookingDto.getItem()), ItemDateDto.class))
                .andExpect(jsonPath("$[0].booker", is(testBookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].status", is(testBookingDto.getStatus().toString()), String.class));
    }

    @Test
    void getAllBookedItemsOfUser() throws Exception {
        Mockito
                .when(bookingService.getAllBookedItemsOfUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(testBookingDto));

        mvc.perform(get("/bookings/owner")
                        .content(objectMapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USERKEY, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item", is(testBookingDto.getItem()), ItemDateDto.class))
                .andExpect(jsonPath("$[0].booker", is(testBookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].status", is(testBookingDto.getStatus().toString()), String.class));
    }
}
