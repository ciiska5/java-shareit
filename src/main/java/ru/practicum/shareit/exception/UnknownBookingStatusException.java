package ru.practicum.shareit.exception;

public class UnknownBookingStatusException extends RuntimeException {
    public UnknownBookingStatusException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public UnknownBookingStatusException(String message) {
        super(message);
    }

    public UnknownBookingStatusException() {

    }
}
