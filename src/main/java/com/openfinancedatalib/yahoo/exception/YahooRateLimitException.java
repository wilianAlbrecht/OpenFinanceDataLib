package com.openfinancedatalib.yahoo.exception;

/**
 * Exception thrown when Yahoo Finance rate limits a request.
 *
 * <p>
 * This exception typically represents an HTTP {@code 429 Too Many Requests}
 * response from Yahoo Finance.
 *
 * <p>
 * It indicates that the client has exceeded Yahoo's request limits
 * and should slow down or temporarily stop sending requests.
 *
 * <p>
 * Errors represented by this exception are usually
 * <b>transient</b>, but should not be retried immediately.
 * Implementations may choose to apply:
 * <ul>
 *   <li>Backoff strategies</li>
 *   <li>Request throttling</li>
 *   <li>Temporary request suspension</li>
 * </ul>
 *
 * <p>
 * This exception is commonly thrown by response validators
 * when Yahoo enforces rate limiting.
 */
public class YahooRateLimitException extends YahooException {

    /**
     * Creates a new {@code YahooRateLimitException}
     * with the specified error message.
     *
     * @param message a human-readable description of the rate limit error
     */
    public YahooRateLimitException(String message) {
        super(message);
    }
}
