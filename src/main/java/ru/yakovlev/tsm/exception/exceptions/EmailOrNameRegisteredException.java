package ru.yakovlev.tsm.exception.exceptions;

public class EmailOrNameRegisteredException extends RuntimeException {
    public EmailOrNameRegisteredException() {
    }

    public EmailOrNameRegisteredException(String message) {
        super(message);
    }

    public EmailOrNameRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailOrNameRegisteredException(Throwable cause) {
        super(cause);
    }
}
