package ru.practicum.shareit.exception;

public class ItemRequestNotFoundException extends RuntimeException {

    public ItemRequestNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }

    public ItemRequestNotFoundException() {

    }
}
