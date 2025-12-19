package com.openfinancedatalib.yahoo.exception;

/**
 * Exception thrown when Yahoo Finance is unavailable or returns
 * an invalid response.
 *
 * <p>
 * This exception represents scenarios where:
 * <ul>
 *   <li>Yahoo returns a 5xx HTTP status code</li>
 *   <li>The response body is empty or malformed</li>
 *   <li>Yahoo returns an explicit error payload</li>
 * </ul>
 *
 * <p>
 * Errors represented by this exception are typically
 * <b>not recoverable immediately</b> and should not
 * be retried automatically.
 *
 * <p>
 * This exception is usually thrown by:
 * <ul>
 *   <li>Response validators</li>
 *   <li>Low-level HTTP clients</li>
 * </ul>
 *
 * <p>
 * Higher layers may choose to log or propagate this
 * exception directly to the caller.
 */
public class YahooUnavailableException extends YahooException {

    /**
     * Creates a new {@code YahooUnavailableException}
     * with the specified error message.
     *
     * @param message a human-readable description of the error
     */
    public YahooUnavailableException(String message) {
        super(message);
    }
}
