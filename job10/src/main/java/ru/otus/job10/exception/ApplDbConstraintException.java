package ru.otus.job10.exception;

public class ApplDbConstraintException extends ApplException {

    public ApplDbConstraintException() {
        super();
    }

    public ApplDbConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplDbConstraintException(String message) {
        super(message);
    }

    public ApplDbConstraintException(Throwable cause) {
        super(cause);
    }

}
