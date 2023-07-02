package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;

import java.util.ArrayList;

@Component
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                ItemRequestMapper.toItemRequestDto(item.getRequest()),
                new ArrayList<>()
        );
    }

    public static ItemDateDto toItemDateDto(Item item) {
        return new ItemDateDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                ItemRequestMapper.toItemRequestDto(item.getRequest())
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }
}
