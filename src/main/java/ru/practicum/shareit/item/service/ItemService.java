package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;

public interface ItemService {

    ItemDto addNewItem(ItemDto itemDto,Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDateDto getItemById(Long userId, Long itemId);

    List<ItemDateDto> getAllItemsOfUser(Long userId);

    List<ItemDto> getItemsByRequestText(Long userId, String text);

    CommentDto addNewCommentForItem(Long userId, Long itemId, CommentDto commentDto);
}
