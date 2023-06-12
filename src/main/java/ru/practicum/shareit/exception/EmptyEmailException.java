package ru.practicum.shareit.exception;

public class EmptyEmailException extends RuntimeException {
    public EmptyEmailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
    public EmptyEmailException(String message) {
        super(message);
    }

    public EmptyEmailException() {

    }
}
