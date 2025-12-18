package com.openfinancedatalib.yahoo.exception;

public class YahooException extends RuntimeException {

    public YahooException(String message) {
        super(message);
    }

    public YahooException(String message, Throwable cause) {
        super(message, cause);
    }
}
