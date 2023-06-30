package ru.practicum.shareit.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException() {

    }
}
