package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemDAO {
    ItemDto addNewItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getAllItemsOfUser(Long userId);

    List<ItemDto> getItemsByRequestText(Long userId, String text);
}
