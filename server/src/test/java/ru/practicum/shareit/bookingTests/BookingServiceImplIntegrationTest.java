package ru.practicum.shareit.bookingTests;

//ИНТЕГРАЦИОННЫЙ ТЕСТ для класса BookingServImpl

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void getAllBookedItemsOfUserTest() {
        UserDto userDto1 = createUser("Altair", "ubisoft@gmail.com");
        UserDto userDto2 = createUser("Almualim", "abstergo@gmail.com");

        ItemDto itemDto11 = createItem("Apple Of Eden", "Real Apple Of Eden", userDto1.getId());
        ItemDto itemDto12 = createItem("Apple Of Eden Fake", "Fake Apple Of Eden", userDto1.getId());

        BookingDto bookingDto = createBooking(
                userDto2.getId(),
                itemDto11.getId(),
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(20)
        );

        List<BookingDto> foundBookedItemsList = bookingService
                .getAllBookedItemsOfUser(userDto1.getId(), "ALL", 0, 1);

        Assertions.assertEquals(1, foundBookedItemsList.size());
        Assertions.assertEquals(bookingDto, foundBookedItemsList.get(0));
        Assertions.assertEquals(bookingDto.getStatus(), foundBookedItemsList.get(0).getStatus());
        Assertions.assertEquals(itemDto11.getId(), foundBookedItemsList.get(0).getItem().getId());
        Assertions.assertNotEquals(itemDto12.getId(), foundBookedItemsList.get(0).getItem().getId());
    }

    private UserDto createUser(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userService.addNewUser(userDto);
    }

    private ItemDto createItem(String name, String description, Long userId) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(Boolean.TRUE);
        return itemService.addNewItem(itemDto, userId);
    }

    private BookingDto createBooking(Long userId, Long itemId, LocalDateTime start, LocalDateTime end) {
        BookingDateDto bookingDateDto = new BookingDateDto();
        bookingDateDto.setItemId(itemId);
        bookingDateDto.setStart(start);
        bookingDateDto.setEnd(end);
        return bookingService.createNewBooking(bookingDateDto, userId);

    }
}
