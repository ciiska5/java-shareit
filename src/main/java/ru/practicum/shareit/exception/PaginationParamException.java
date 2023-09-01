package ru.practicum.shareit.exception;

public class PaginationParamException extends RuntimeException{

    public PaginationParamException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public PaginationParamException(String message) {
        super(message);
    }

    public PaginationParamException() {

    }

}
