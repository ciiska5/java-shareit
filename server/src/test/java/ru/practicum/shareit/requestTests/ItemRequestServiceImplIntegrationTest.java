package ru.practicum.shareit.requestTests;

//ИНТЕГРАЦИОННЫЙ ТЕСТ для класса ItemRequestServiceImpl

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    void getAllItemRequestsTest() {
        UserDto userDto1 = createUser("Altair", "ubisoft@gmail.com");
        UserDto userDto2 = createUser("Almualim", "abstergo@gmail.com");

        ItemRequestDto itemRequestDto11 = createItemRequestDto("Apple Of Eden", userDto2.getId());
        ItemRequestDto itemRequestDto12 = createItemRequestDto("Apple Of Eden Fake", userDto2.getId());

        List<ItemRequestDto> foundItemRequestDtoList = itemRequestService
                .getAllItemRequests(userDto1.getId(), 0, 2);

        Assertions.assertEquals(2, foundItemRequestDtoList.size());
        Assertions.assertEquals(itemRequestDto11, foundItemRequestDtoList.get(0));
        Assertions.assertEquals(itemRequestDto11.getDescription(), foundItemRequestDtoList.get(0).getDescription());
        Assertions.assertEquals(itemRequestDto12, foundItemRequestDtoList.get(1));
        Assertions.assertEquals(itemRequestDto12.getDescription(), foundItemRequestDtoList.get(1).getDescription());
    }

    private UserDto createUser(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        return userService.addNewUser(userDto);
    }

    private ItemRequestDto createItemRequestDto(String description, Long userId) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(description);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setItems(List.of());
        return itemRequestService.addNewRequest(userId, itemRequestDto);
    }
}
