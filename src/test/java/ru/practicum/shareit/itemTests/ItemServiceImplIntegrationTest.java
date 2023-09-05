package ru.practicum.shareit.itemTests;

//ИНТЕГРАЦИОННЫЙ ТЕСТ для класса ItemServiceImpl

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void getItemsByRequestTextTest() {
        UserDto userDto1 = createUser("Altair", "ubisoft@gmail.com");
        UserDto userDto2 = createUser("Almualim", "abstergo@gmail.com");

        ItemDto itemDto11 = createItem("Apple Of Eden", "Real Apple Of Eden", userDto1.getId());
        ItemDto itemDto12 = createItem("Apple Of Eden Fake", "Fake Apple Of Eden", userDto1.getId());

        List<ItemDto> actualItemDtosList = itemService
                .getItemsByRequestText(userDto2.getId(), "ApPle", 0, 2);

        Assertions.assertEquals(2, actualItemDtosList.size());
        Assertions.assertEquals(itemDto11, actualItemDtosList.get(0));
        Assertions.assertEquals(itemDto11.getDescription(), actualItemDtosList.get(0).getDescription());
        Assertions.assertEquals(itemDto12, actualItemDtosList.get(1));
        Assertions.assertEquals(itemDto12.getDescription(), actualItemDtosList.get(1).getDescription());
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
}
