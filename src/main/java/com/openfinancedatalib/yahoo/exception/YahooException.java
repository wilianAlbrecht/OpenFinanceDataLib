package com.openfinancedatalib.yahoo.exception;

/**
 * Base exception for all Yahoo Finance related errors
 * thrown by the OpenFinanceData library.
 *
 * <p>
 * This exception serves as the root of the Yahoo-specific
 * exception hierarchy, allowing consumers to:
 * <ul>
 *   <li>Catch all Yahoo-related errors with a single type</li>
 *   <li>Differentiate Yahoo errors from other runtime exceptions</li>
 * </ul>
 *
 * <p>
 * All exceptions extending this class represent errors
 * originating from Yahoo Finance responses, authentication,
 * rate limiting, or service availability.
 *
 * <p>
 * This class extends {@link RuntimeException} by design,
 * keeping the library API clean and avoiding mandatory
 * checked exception handling.
 */
public class YahooException extends RuntimeException {

    /**
     * Creates a new {@code YahooException} with the specified message.
     *
     * @param message a human-readable description of the error
     */
    public YahooException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code YahooException} with the specified message
     * and underlying cause.
     *
     * @param message a human-readable description of the error
     * @param cause the underlying cause of the exception
     */
    public YahooException(String message, Throwable cause) {
        super(message, cause);
    }
}
