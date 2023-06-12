package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemDaoImpl implements ItemDAO {

    private final Map<Long, List<Item>> items = new HashMap<>();
    private final UserDAO userDAO;

    @Override
    public ItemDto addNewItem(ItemDto itemDto, Long userId) {
        checkUsersExistenceById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(getId());
        item.setUserId(userId);

        items.compute(userId, (userIds, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        log.info("Пользователем с id = {} добавлена вещь c id = {}.", userId, item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        checkUsersExistenceById(userId);
        if (items.get(userId) == null) {
            log.error("У пользователя с id = {} нет вещей для аренды. ", userId);
            throw new ItemNotFoundException("У пользователя с id = " + userId + " нет вещей для аренды.");
        }

        Optional<Item> optionalItem = items.get(userId).stream()
                .filter(item1 -> item1.getId().equals(itemId))
                .findFirst();

        String itemName = itemDto.getName();
        String itemDescription = itemDto.getDescription();
        Boolean itemAvailable = itemDto.getAvailable();

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (itemName != null) {
                item.setName(itemDto.getName());
            }
            if (itemDescription != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemAvailable != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            log.info("У пользователя с id = {} обновлена вещь с id = {}. ", userId, itemId);
            return ItemMapper.toItemDto(item);
        }

        log.error("У пользователя с id = {} нет вещи с id = {} в аренде", userId, itemId);
        throw new ItemNotFoundException("У пользователя с id = " + userId + " нет вещей для аренды");
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        checkUsersExistenceById(userId);

        Optional<ItemDto> itemDto = getAllItemsDto()
                .stream()
                .filter(itemDto1 -> itemDto1.getId().equals(itemId))
                .findFirst();

        if (itemDto.isEmpty()) {
            log.error("Вещь с id = {} не найдена", itemId);
            throw new ItemNotFoundException("Вещь с id = " + itemId + " не найдена");
        }

        log.info("Получена вещь с id = {}.", itemId);
        return itemDto.get();
    }

    @Override
    public List<ItemDto> getAllItemsOfUser(Long userId) {
        checkUsersExistenceById(userId);
        List<ItemDto> userItems = new ArrayList<>();
        items.get(userId).forEach(item -> userItems.add(ItemMapper.toItemDto(item)));
        log.info("Пользователем с id = {} получены все его вещи. ", userId);
        return userItems;
    }

    @Override
    public List<ItemDto> getItemsByRequestText(Long userId, String text) {
        List<ItemDto> foundItemsDto = new ArrayList<>();
        if (!text.isEmpty()) {
            String lowerCaseText = text.toLowerCase();
            foundItemsDto = getAllItemsDto()
                    .stream()
                    .filter(itemDto -> itemDto.getAvailable().equals(Boolean.TRUE) &&
                            ((itemDto.getName().toLowerCase().contains(lowerCaseText)) ||
                            itemDto.getDescription().toLowerCase().contains(lowerCaseText)))
                    .collect(Collectors.toList());

            if (foundItemsDto.isEmpty()) {
                log.info("Для пользователя с id = {} по его запросу \"{}\" ничего не найдено.", userId, text);
            } else {
                log.info("Для пользователя с id = {} по его запросу \"{}\" количество найденных результатов равно {}",
                        userId, text, foundItemsDto.size());
            }
        } else {
            log.error("Пользователем с id = {} задан пустой поисковый запрос.", userId);
        }

        return foundItemsDto;
    }

    //получение id для вещи
    private long lastId = 1;

    private Long getId() {
        return lastId++;
    }

    //проверка пользователя на существование
    private void checkUsersExistenceById(Long userId) {
        if (userDAO.getUserById(userId) == null) {
            log.error("Пользователь с id = {} не найден. ", userId);
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден.");
        }
    }

    //получение всех вещей
    private List<ItemDto> getAllItemsDto() {
        List<ItemDto> allItemsDto = new ArrayList<>();
        items.values()
                .forEach(items1 -> items1
                        .forEach(item -> allItemsDto.add(ItemMapper.toItemDto(item))));

        return allItemsDto;
    }
}
