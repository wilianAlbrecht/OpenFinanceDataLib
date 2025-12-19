package com.openfinancedatalib.yahoo.exception;

/**
 * Exception thrown when a Yahoo Finance request fails
 * due to authentication or authorization issues.
 *
 * <p>
 * This exception typically represents scenarios such as:
 * <ul>
 *   <li>Expired or invalid crumb</li>
 *   <li>Invalid or expired Yahoo session cookies</li>
 *   <li>Unauthorized or forbidden HTTP responses (401 / 403)</li>
 *   <li>Unexpected HTML responses instead of JSON</li>
 * </ul>
 *
 * <p>
 * Errors represented by this exception are often
 * <b>recoverable</b> by:
 * <ul>
 *   <li>Clearing cached crumbs</li>
 *   <li>Re-initializing the Yahoo session</li>
 *   <li>Requesting a new crumb</li>
 * </ul>
 *
 * <p>
 * For this reason, higher-level components such as
 * {@code YahooRequestCoordinator} may automatically
 * retry the request once when this exception is thrown.
 */
public class YahooAuthException extends YahooException {

    /**
     * Creates a new {@code YahooAuthException}
     * with the specified error message.
     *
     * @param message a human-readable description of the authentication error
     */
    public YahooAuthException(String message) {
        super(message);
    }
}
