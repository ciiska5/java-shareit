package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    //добавить новый запрос вещи
    ItemRequestDto addNewRequest(Long userId, ItemRequestDto itemRequestDto);

    //получить список своих запросов вместе с данными об ответах на них от более новых к более старым
    List<ItemRequestDto> getAllByRequestor(Long userId);

    //получить постранично список запросов, созданных другими пользователями от более новых к более старым.
    //для одной страницы: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size);

    //получить данные об одном конкретном запросе вместе с данными об ответах на него
    ItemRequestDto getItemRequestById(Long userId, Long requestId);
}
