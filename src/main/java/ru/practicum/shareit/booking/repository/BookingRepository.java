package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    //все букинги арендодатора со статусом ALL
    Page<Booking> findAllByBooker(User booker, Pageable pageable);

    //все букинги арендодатора со статусом CURRENT
    Page<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    //все букинги арендодатора со статусом PAST
    Page<Booking> findAllByBookerAndEndBefore(
            User booker, LocalDateTime end, Pageable pageable
    );

    //все букинги арендодатора со статусом FUTURE
    Page<Booking> findAllByBookerAndStartAfter(
            User booker, LocalDateTime start, Pageable pageable
    );

    //все букинги арендодатора со статусом WAITING и REJECTED
    Page<Booking> findAllByBookerAndStatusEquals(
            User booker, BookingStatus status, Pageable pageable
    );

    //все букинги арендодателя со статусом ALL
    Page<Booking> findAllByItemOwner(User owner, Pageable pageable);

    //все букинги арендодателя со статусом CURRENT
    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    //все букинги арендодателя со статусом PAST
    Page<Booking> findAllByItemOwnerAndEndBefore(
            User owner, LocalDateTime end, Pageable pageable
    );

    //все букинги арендодателя со статусом FUTURE
    Page<Booking> findAllByItemOwnerAndStartAfter(
            User owner, LocalDateTime start, Pageable pageable
    );

    //все букинги арендодателя со статусом WAITING и REJECTED
    Page<Booking> findAllByItemOwnerAndStatusEquals(
            User owner, BookingStatus status, Pageable pageable
    );

    //получение букингов для определённой вещи по возрастанию даты начала бронирования
    List<Booking> findAllByItemIdOrderByStartAsc(Long itemId);

    List<Booking> findAllByItemOwnerIdOrderByStartAsc(Long ownerId);

    //получение букингов вещей, которые совершал пользователь до оставления комментариев к этим вещам
    List<Booking> findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
            Long bookerId, Long itemId, BookingStatus status, LocalDateTime end
    );

    //получение букингов для определённой вещи по возрастанию даты начала бронирования со статусом APPROVED
    List<Booking> findAllByItemIdAndStatusEqualsOrderByStartAsc(
            Long itemId, BookingStatus status
    );
}
