package ru.kpfu.itis.lobanov.exceptions;

public class LocalHostException extends RuntimeException {
    public LocalHostException() {
    }

    public LocalHostException(String message) {
        super(message);
    }

    public LocalHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalHostException(Throwable cause) {
        super(cause);
    }

    public LocalHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
