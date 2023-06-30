package ru.practicum.shareit.exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException() {

    }
}