package ru.otus.job07.exception;

public class ApplDbNoDataFoundException extends ApplException {

    public ApplDbNoDataFoundException() {
        super();
    }

    public ApplDbNoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplDbNoDataFoundException(String message) {
        super(message);
    }

    public ApplDbNoDataFoundException(Throwable cause) {
        super(cause);
    }

}
