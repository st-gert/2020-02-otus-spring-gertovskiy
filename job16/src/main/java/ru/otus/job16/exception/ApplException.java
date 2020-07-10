package ru.otus.job16.exception;

public class ApplException extends RuntimeException {

    public ApplException() {
        super();
    }

    public ApplException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplException(String message) {
        super(message);
    }

    public ApplException(Throwable cause) {
        super(cause);
    }

}
