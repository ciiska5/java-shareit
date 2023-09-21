package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createNewBooking(@RequestBody @Valid BookItemRequestDto requestDto,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createNewBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Approve status of booking {}", bookingId);
        return bookingClient.approveRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "15", required = false) @Min(1) Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsOfUser(userId, stateParam, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookedItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "15", required = false) @Min(1) Integer size) {
        log.info("Get owner's booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookedItemsOfUser(userId, stateParam, from, size);
    }
}
