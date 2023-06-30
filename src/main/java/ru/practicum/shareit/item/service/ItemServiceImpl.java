package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;

    private final UserDAO userDAO;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, Long userId) {
        checkUsersExistenceById(userId);
        Item item = ItemMapper.toItem(itemDto);
        User user = userDAO.getUserById(userId);
        item.setUser(user);
        itemDAO.addNewItem(item, userId);
        log.info("Пользователем с id = {} добавлена вещь c id = {}.", userId, item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        checkUsersExistenceById(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemDAO.updateItem(item, userId, itemId));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        checkUsersExistenceById(userId);
        Item item = itemDAO.getItemById(userId, itemId);
        if (item == null) {
            log.error("Вещь с id = {} не найдена", itemId);
            throw new ItemNotFoundException("Вещь с id = " + itemId + " не найдена");
        } else {
            log.info("Получена вещь с id = {}.", itemId);
            return ItemMapper.toItemDto(item);
        }
    }

    @Override
    public List<ItemDto> getAllItemsOfUser(Long userId) {
        checkUsersExistenceById(userId);
        List<Item> userItems = itemDAO.getAllItemsOfUser(userId);
        List<ItemDto> userItemsDto = new ArrayList<>();
        userItems.forEach(item -> userItemsDto.add(ItemMapper.toItemDto(item)));
        return userItemsDto;
    }

    @Override
    public List<ItemDto> getItemsByRequestText(Long userId, String text) {
        List<Item> foundItems = itemDAO.getItemsByRequestText(userId, text);
        List<ItemDto> foundItemsDto = new ArrayList<>();
        foundItems.forEach(item -> foundItemsDto.add(ItemMapper.toItemDto(item)));
        return foundItemsDto;
    }

    //проверка пользователя на существование
    private void checkUsersExistenceById(Long userId) {
        if (userDAO.getUserById(userId) == null) {
            log.error("Пользователь с id = {} не найден. ", userId);
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден.");
        }
    }
}
