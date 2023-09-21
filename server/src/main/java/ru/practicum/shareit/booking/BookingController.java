package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Класс-контроллер сущности Booking
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody BookingDateDto bookingDateDto) {
        return bookingService.createNewBooking(bookingDateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveRequest(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam Boolean approved) {
        return bookingService.approveRequest(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL", required = false) String state,
                                                 @RequestParam(defaultValue = "0", required = false) int from,
                                                 @RequestParam(defaultValue = "15", required = false) int size) {
        return bookingService.getAllBookingsOfUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookedItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL", required = false) String state,
                                                    @RequestParam(defaultValue = "0", required = false) int from,
                                                    @RequestParam(defaultValue = "15", required = false) int size) {
        return bookingService.getAllBookedItemsOfUser(userId, state, from, size);
    }
}
