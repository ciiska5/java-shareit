package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
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

    //просмотр информации о конкретной вещи (любой пользователь)
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    //просмотр всех вещей владельца (только самим владельцем)
    @GetMapping
    public List<ItemDto> getAllItemsOfUser (@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsOfUser(userId);
    }

    //поиск всех вещей арендодатором по совпадению слов в названии или описании
    @GetMapping("/search")
    public List<ItemDto> getItemsByRequestText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "text") String text) {
        return itemService.getItemsByRequestText(userId, text);
    }
 }
