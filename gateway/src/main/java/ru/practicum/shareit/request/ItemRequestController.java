package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Класс-контроллер сущности ItemRequest
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    //добавить новый запрос вещи
    @PostMapping
    public ResponseEntity<Object> addNewRequest(@RequestHeader("X-Sharer-User-Id")Long userId,
                                                @Valid @RequestBody ItemRequestRequestDto requestDto) {
        log.info("Create item request by user {}", userId);
        return itemRequestClient.addNewRequest(userId, requestDto);
    }

    //получить список своих запросов вместе с данными об ответах на них от более новых к более старым
    @GetMapping
    public ResponseEntity<Object> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all user {} item requests", userId);
        return itemRequestClient.getAllByRequestor(userId);
    }

    //получить постранично список запросов, созданных другими пользователями от более новых к более старым.
    //для одной страницы: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
                                                   @RequestParam(defaultValue = "15", required = false) @Min(1) int size) {
        log.info("Get all item requests of user={}", userId);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    //получить данные об одном конкретном запросе вместе с данными об ответах на него
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        log.info("Get item request {}", requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

}
