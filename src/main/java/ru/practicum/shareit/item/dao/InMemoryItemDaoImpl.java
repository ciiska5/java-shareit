package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemDaoImpl implements ItemDAO {

    private final Map<Long, List<Item>> usersItems = new HashMap<>(); //userId:userItemsList
    private final Map<Long, Item> items = new HashMap<>();//itemId:item

    @Override
    public void addNewItem(Item item, Long userId) {
        Long itemId = getId();
        item.setId(itemId);
        items.put(itemId, item);

        usersItems.compute(userId, (userIds, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        List<Item> userItems = usersItems.get(userId);
        Item updatingItem = items.get(itemId);
        if (userItems == null) {
            log.error("У пользователя с id = {} нет вещей для аренды. ", userId);
            throw new ItemNotFoundException("У пользователя с id = " + userId + " нет вещей для аренды.");
        }
        if (updatingItem == null) {
            log.error("Вещь с id = {} не найдена. ", itemId);
            throw new ItemNotFoundException("Вещь с id = " + itemId + " нет найдена.");
        } else {
            String itemName = item.getName();
            String itemDescription = item.getDescription();
            Boolean itemAvailable = item.getAvailable();
            if (itemName != null) {
                updatingItem.setName(itemName);
            }
            if (itemDescription != null) {
                updatingItem.setDescription(itemDescription);
            }
            if (itemAvailable != null) {
                updatingItem.setAvailable(itemAvailable);
            }
            log.info("У пользователя с id = {} обновлена вещь с id = {}. ", userId, itemId);
            return updatingItem;
        }
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsOfUser(Long userId) {
        log.info("Пользователем с id = {} получены все его вещи. ", userId);
        return usersItems.get(userId);
    }

    @Override
    public List<Item> getItemsByRequestText(Long userId, String text) {
        List<Item> foundItems = new ArrayList<>();
        if (!text.isEmpty()) {
            String lowerCaseText = text.toLowerCase();
            foundItems = items
                    .values()
                    .stream()
                    .filter(item -> item.getAvailable().equals(Boolean.TRUE) &&
                            ((item.getName().toLowerCase().contains(lowerCaseText)) ||
                            item.getDescription().toLowerCase().contains(lowerCaseText)))
                    .collect(Collectors.toList());

            if (foundItems.isEmpty()) {
                log.info("Для пользователя с id = {} по его запросу \"{}\" ничего не найдено.", userId, text);
            } else {
                log.info("Для пользователя с id = {} по его запросу \"{}\" количество найденных результатов равно {}",
                        userId, text, foundItems.size());
            }
        } else {
            log.error("Пользователем с id = {} задан пустой поисковый запрос.", userId);
        }

        return foundItems;
    }

    //получение id для вещи
    private long lastId = 1;

    private Long getId() {
        return lastId++;
    }
}
