package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, Long userId) {
        return itemDAO.addNewItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        return itemDAO.updateItem(itemDto, userId, itemId);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        return itemDAO.getItemById(userId, itemId);
    }

    @Override
    public List<ItemDto> getAllItemsOfUser (Long userId) {
        return itemDAO.getAllItemsOfUser(userId);
    }

    @Override
    public List<ItemDto> getItemsByRequestText(Long userId, String text) {
        return itemDAO.getItemsByRequestText(userId, text);
    }
}
