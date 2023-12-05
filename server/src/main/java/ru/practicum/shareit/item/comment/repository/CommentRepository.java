package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //получение всех комментариев вещи по его id
    List<Comment> findAllByItemId(Long itemId);

    //получение всех комментариев к списку вещей
    List<Comment> findAllByItemIn(List<Item> items);
}
