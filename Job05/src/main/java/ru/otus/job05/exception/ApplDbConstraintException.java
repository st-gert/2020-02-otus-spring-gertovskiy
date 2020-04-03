package ru.otus.job05.exception;

public class ApplDbConstraintException extends Exception {

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
