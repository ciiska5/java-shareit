package ru.practicum.shareit.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {

    }
}
