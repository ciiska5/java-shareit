package ru.practicum.shareit.exception;

public class ItemAlreadyBookedException extends RuntimeException {

    public ItemAlreadyBookedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ItemAlreadyBookedException(String message) {
        super(message);
    }

    public ItemAlreadyBookedException() {

    }
}
