package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер сущности Item
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //добавление новой вещи
    @PostMapping
    public ItemDto addNewItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addNewItem(itemDto, userId);
    }

    //редактирование вещи (изменение названия, описания и статуса) (только владелец вещи)
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    //просмотр информации о конкретной вещи пользователся с userId (любой пользователь)
    @GetMapping("/{itemId}")
    public ItemDateDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    //просмотр всех вещей владельца (только самим владельцем)
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping
    public List<ItemDateDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "15") int size) {
        return itemService.getAllItemsOfUser(userId, from, size);
    }

    //поиск всех вещей арендодатором по совпадению слов в названии или описании
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/search")
    public List<ItemDto> getItemsByRequestText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "text") String text,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "15") int size) {
        return itemService.getItemsByRequestText(userId, text, from, size);
    }

    //добавление пользователем (userId) отзыва (commentDto) на вещь (itemId)
    @PostMapping("/{itemId}/comment")
    public CommentDto addNewCommentForItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        return itemService.addNewCommentForItem(userId, itemId, commentDto);
    }
 }
