package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Класс-контроллер сущности Item
 */
@Controller
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    //добавление новой вещи
    @PostMapping
    public ResponseEntity<Object> addNewItem(@Valid @RequestBody ItemRequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Create item");
        return itemClient.addNewItem(userId, requestDto);
    }

    //редактирование вещи (изменение названия, описания и статуса) (только владелец вещи)
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemRequestDto requestDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        log.info("Get all items from user {}", userId);
        return itemClient.updateItem(requestDto, userId, itemId);
    }

    //просмотр информации о конкретной вещи пользователся с userId (любой пользователь)
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId) {
        log.info("Get item {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    //просмотр всех вещей владельца (только самим владельцем)
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                                               @RequestParam(defaultValue = "15", required = false) @Min(1) int size) {
        log.info("Get all items from user {}", userId);
        return itemClient.getAllItemsOfUser(userId, from, size);
    }

    //поиск всех вещей арендодатором по совпадению слов в названии или описании
    //параметры пагинации: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByRequestText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "text") String text,
                                               @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                                               @RequestParam(defaultValue = "15", required = false) @Min(1) int size) {
        log.info("Search items by text {}", text);
        return itemClient.getItemsByRequestText(userId, text, from, size);
    }

    //добавление пользователем (userId) отзыва (commentDto) на вещь (itemId)
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewCommentForItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Create comment to item {}", itemId);
        return itemClient.addNewCommentForItem(userId, itemId, commentRequestDto);
    }
 }
