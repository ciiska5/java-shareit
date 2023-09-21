package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Шаблон объекта Booking для хранилища
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор бронирования

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start; // дата и время начала бронирования

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end; // дата и время конца бронирования

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;// вещь, которую пользователь бронирует

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker; // пользователь, который осуществляет бронирование;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private BookingStatus status; // статус бронирования
}
