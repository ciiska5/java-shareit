package ru.practicum.shareit.exception;

public class BookingDateException extends RuntimeException {
    public BookingDateException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public BookingDateException(String message) {
        super(message);
    }

    public BookingDateException() {

    }
}
