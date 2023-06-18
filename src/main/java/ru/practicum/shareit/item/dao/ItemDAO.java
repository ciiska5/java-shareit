package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDAO {
    void addNewItem(Item item, Long userId);

    Item updateItem(Item item, Long userId, Long itemId);

    Item getItemById(Long userId, Long itemId);

    List<Item> getAllItemsOfUser(Long userId);

    List<Item> getItemsByRequestText(Long userId, String text);
}
