package ru.practicum.shareit.exception;

public class BookingBadRequest extends RuntimeException{
    public BookingBadRequest(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public BookingBadRequest(String message) {
        super(message);
    }

    public BookingBadRequest() {

    }
}
