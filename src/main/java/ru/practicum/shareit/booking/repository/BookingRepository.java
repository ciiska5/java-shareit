package ru.practicum.shareit.booking.repository;

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
    List<Booking> findAllByBooker(User booker, Sort sort);

    //все букинги арендодатора со статусом CURRENT
    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker, LocalDateTime start, LocalDateTime end, Sort sort
    );

    //все букинги арендодатора со статусом PAST
    List<Booking> findAllByBookerAndEndBefore(
            User booker, LocalDateTime end, Sort sort
    );

    //все букинги арендодатора со статусом FUTURE
    List<Booking> findAllByBookerAndStartAfter(
            User booker, LocalDateTime start, Sort sort
    );

    //все букинги арендодатора со статусом WAITING и REJECTED
    List<Booking> findAllByBookerAndStatusEquals(
            User booker, BookingStatus status, Sort sort
    );

    //все букинги арендодателя со статусом ALL
    List<Booking> findAllByItemOwner(User owner, Sort sort);

    //все букинги арендодателя со статусом CURRENT
    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner, LocalDateTime start, LocalDateTime end, Sort sort
    );

    //все букинги арендодателя со статусом PAST
    List<Booking> findAllByItemOwnerAndEndBefore(
            User owner, LocalDateTime end, Sort sort
    );

    //все букинги арендодателя со статусом FUTURE
    List<Booking> findAllByItemOwnerAndStartAfter(
            User owner, LocalDateTime start, Sort sort
    );

    //все букинги арендодателя со статусом WAITING и REJECTED
    List<Booking> findAllByItemOwnerAndStatusEquals(
            User owner, BookingStatus status, Sort sort
    );

    //получение букингов для определённой вещи по возрастанию даты начала бронирования
    List<Booking> findAllByItemIdOrderByStartAsc(Long itemId);

    //получение букингов вещей, которые совершал пользователь до оставления комментариев к этим вещам
    List<Booking> findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
            Long bookerId, Long itemId, BookingStatus status, LocalDateTime end
    );

    //получение букингов для определённой вещи по возрастанию даты начала бронирования со статусом APPROVED
    List<Booking> findAllByItemIdAndStatusEqualsOrderByStartAsc(
            Long itemId, BookingStatus status
    );
}
