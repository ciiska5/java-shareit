package ru.practicum.shareit.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException() {

    }
}
