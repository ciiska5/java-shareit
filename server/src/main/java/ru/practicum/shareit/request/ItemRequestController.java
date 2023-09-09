package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Класс-контроллер сущности ItemRequest
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    //добавить новый запрос вещи
    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader("X-Sharer-User-Id")Long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addNewRequest(userId, itemRequestDto);
    }

    //получить список своих запросов вместе с данными об ответах на них от более новых к более старым
    @GetMapping
    public List<ItemRequestDto> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllByRequestor(userId);
    }

    //получить постранично список запросов, созданных другими пользователями от более новых к более старым.
    //для одной страницы: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0", required = false) int from,
                                                   @RequestParam(defaultValue = "15", required = false) int size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    //получить данные об одном конкретном запросе вместе с данными об ответах на него
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

}
