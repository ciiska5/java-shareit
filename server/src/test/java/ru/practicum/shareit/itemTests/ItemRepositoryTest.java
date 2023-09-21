package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

//тест для метода с кастомным запросом репозитория ItemRepository

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    private User initTestUser() {
        User testUser = new User();
        testUser.setName("Altair");
        testUser.setEmail("ubisoft@gmail.com");
        return testUser;
    }

    private Item initTestItem(User owner) {
        Item testItem = new Item();
        testItem.setName("Eden's Apple");
        testItem.setDescription("Real Apple of Eden");
        testItem.setAvailable(Boolean.TRUE);
        testItem.setOwner(owner);
        return testItem;
    }

    @Test
    void findAllItemsByTextTest() {
        User user = initTestUser();
        em.persist(user);
        Item item = initTestItem(user);
        em.persist(item);

        List<Item> foundItemsList = itemRepository.findAllItemsByText("real apple", PageRequest.of(0, 1)).toList();

        Assertions.assertEquals(1, foundItemsList.size());
        Assertions.assertEquals(item, foundItemsList.get(0));
    }
}
