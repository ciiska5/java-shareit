package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.PaginationParamException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class ItemRequestServiceTest {

    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);

    private ItemRequestServiceImpl itemRequestService;
    private Item testItem;
    private ItemRequest testItemRequest;

    @BeforeEach
    void initItem() {
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Sword");
        testItem.setDescription("Sharp sword for enemies");
        testItem.setAvailable(Boolean.TRUE);
        testItem.setRequest(new ItemRequest());
    }

    @BeforeEach
    void initItemRequest() {
        testItemRequest = new ItemRequest();
        testItemRequest.setId(1L);
        testItemRequest.setDescription("Sharp sword");
        testItemRequest.setRequestor(new User());
    }

    @BeforeEach
    void initItemRequestService() {
        itemRequestService = new ItemRequestServiceImpl(
                mockItemRepository, mockUserRepository, mockItemRequestRepository
        );
    }

    @Test
    void addNewRequestTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(testItemRequest);
        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(testItemRequest);

        ItemRequestDto savedRequest = itemRequestService.addNewRequest(1L, testItemRequestDto);

        Assertions.assertEquals(savedRequest.getId(), testItemRequestDto.getId());
        Assertions.assertEquals(savedRequest.getDescription(), testItemRequestDto.getDescription());
        Assertions.assertNotNull(savedRequest.getCreated());
        Assertions.assertEquals(savedRequest.getItems(), new ArrayList<>());
    }

    @Test
    void addNewRequestByNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(testItemRequest);

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemRequestService.addNewRequest(2L, testItemRequestDto)
        );

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void getAllRequestsByReqestorTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(anyLong()))
                .thenReturn(List.of(testItemRequest));
        Mockito
                .when(mockItemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(testItem));
        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(testItemRequest);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        testItemRequestDto.setItems(List.of(testItemDto));
        List<ItemRequestDto> testRequestList = List.of(testItemRequestDto);

        List<ItemRequestDto> foundRequestsList = itemRequestService.getAllByRequestor(1L);

        Assertions.assertEquals(foundRequestsList, testRequestList);
    }

    @Test
    void getAllRequestsByWrongReqestorTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemRequestService.getAllByRequestor(2L));

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void getAllItemRequestsTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findAllByRequestorNotLikeOrderByCreatedAsc(
                        any(User.class), any(Pageable.class))
                )
                .thenReturn(new PageImpl<>(List.of(testItemRequest)));
        Mockito
                .when(mockItemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(testItem));

        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(testItemRequest);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        testItemRequestDto.setItems(List.of(testItemDto));
        List<ItemRequestDto> testRequestList = List.of(testItemRequestDto);

        List<ItemRequestDto> foundRequestList = itemRequestService.getAllItemRequests(1L, 0, 1);

        Assertions.assertEquals(testRequestList, foundRequestList);
    }

    @Test
    void getAllItemRequestsOfNotExistingUserTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemRequestService.getAllItemRequests(2L, 0, 1));

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void getAllItemRequestsWithIncorrectPaginationParamsTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        PaginationParamException error = Assertions.assertThrows(
                PaginationParamException.class, () -> itemRequestService.getAllItemRequests(2L, -1, 0));

        Assertions.assertEquals("Некорректно заданы параметры пагинации", error.getMessage());
    }

    @Test
    void getItemRequestByIdTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItemRequest));
        Mockito
                .when(mockItemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(testItem));

        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(testItemRequest);
        ItemDto testItemDto = ItemMapper.toItemDto(testItem);
        testItemRequestDto.setItems(List.of(testItemDto));

        ItemRequestDto foundItemRequestDto = itemRequestService.getItemRequestById(1L, 1L);

        Assertions.assertEquals(testItemRequestDto, foundItemRequestDto);
    }

    @Test
    void getItemRequestOfNotExistingUserByIdTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException error = Assertions.assertThrows(
                UserNotFoundException.class, () -> itemRequestService.getItemRequestById(2L, 1L));

        Assertions.assertEquals("Пользователь с id = 2 не найден.", error.getMessage());
    }

    @Test
    void getNotExistingItemRequestByIdTest() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        Mockito
                .when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ItemRequestNotFoundException error = Assertions.assertThrows(
                ItemRequestNotFoundException.class, () -> itemRequestService.getItemRequestById(2L, 1L));

        Assertions.assertEquals("Запрос с id = 1 не найден.", error.getMessage());
    }
}
