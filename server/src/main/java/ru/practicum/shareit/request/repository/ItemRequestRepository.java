package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    //получение списка своих запросов реквестором вместе с данными об ответах на них от более новых к более старым
    List<ItemRequest> findAllByRequestorIdOrderByCreatedAsc(Long requestorId);

    //получение списока запросов постранично, созданных другими пользователями от более новых к более старым
    Page<ItemRequest> findAllByRequestorNotLikeOrderByCreatedAsc(User requestor, Pageable pageable);
}
