package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    //поиск предметов по id пользователя
    List<Item> findAllItemsByOwnerId(Long ownerId);

    //поиск вещей по тексту
    @Query(
            value = "SELECT * FROM items i" +
                    " WHERE LOWER(i.name) LIKE CONCAT('%', :text, '%')" +
                    " OR " +
                    " LOWER(i.description) LIKE CONCAT('%', :text, '%')",
            nativeQuery = true
    )
    List<Item> findAllItemsByText(@Param("text") String text);
}
