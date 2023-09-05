package ru.practicum.shareit.exception;

public class UnknownBookingStatusException extends RuntimeException {
    public UnknownBookingStatusException(String message) {
        super(message);
    }
}
