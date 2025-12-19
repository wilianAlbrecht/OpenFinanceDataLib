package com.openfinancedatalib.yahoo.validator;

import com.openfinancedatalib.yahoo.exception.YahooAuthException;
import com.openfinancedatalib.yahoo.exception.YahooInvalidSymbolException;
import com.openfinancedatalib.yahoo.exception.YahooRateLimitException;
import com.openfinancedatalib.yahoo.exception.YahooUnavailableException;

/**
 * Validates HTTP responses received from Yahoo Finance.
 *
 * <p>
 * This class centralizes all response validation logic in a single place,
 * ensuring consistent error handling across the entire library.
 *
 * <p>
 * The validator is responsible for:
 * <ul>
 * <li>Interpreting HTTP status codes</li>
 * <li>Detecting invalid or unexpected response bodies</li>
 * <li>Translating Yahoo errors into domain-specific exceptions</li>
 * </ul>
 *
 * <p>
 * The thrown exceptions are intentionally specific, allowing higher layers
 * (such as {@code YahooRequestCoordinator}) to decide whether a retry
 * is possible or the error should be propagated.
 */
public class YahooResponseValidator {

    /**
     * Validates a Yahoo Finance HTTP response.
     *
     * <p>
     * This method performs two levels of validation:
     * <ol>
     * <li>HTTP status code validation</li>
     * <li>Response body content validation</li>
     * </ol>
     *
     * <p>
     * If any validation rule fails, a runtime exception specific to the
     * error scenario is thrown.
     *
     * @param statusCode HTTP status code returned by Yahoo
     * @param body       response body returned by Yahoo
     *
     * @throws YahooAuthException        if the request is unauthorized or returns
     *                                   HTML
     * @throws YahooRateLimitException   if Yahoo rate limits the request
     * @throws YahooUnavailableException if Yahoo is unavailable or returns
     *                                   an invalid/empty payload
     */
    public static void validate(int statusCode, String body) {

        // ----------------------------------
        // HTTP STATUS VALIDATION
        // ----------------------------------

        // Authentication / authorization failure
        // Typically caused by an expired crumb or invalid session
        if (statusCode == 401 || statusCode == 403) {
            throw new YahooAuthException("Unauthorized request to Yahoo");
        }

        // Rate limit exceeded
        // Indicates that requests should not be retried immediately
        if (statusCode == 429) {
            throw new YahooRateLimitException("Yahoo rate limit exceeded");
        }

        // Server-side error
        // Yahoo is unavailable or experiencing internal issues
        if (statusCode >= 500) {
            throw new YahooUnavailableException(
                    "Yahoo service unavailable (status " + statusCode + ")");
        }

        // ----------------------------------
        // RESPONSE BODY VALIDATION
        // ----------------------------------

        // Empty or missing response body
        // Indicates an invalid or incomplete response
        if (body == null || body.isBlank()) {
            throw new YahooUnavailableException("Empty response from Yahoo");
        }

        // HTML response instead of JSON
        // Yahoo sometimes returns HTML when authentication fails
        if (body.startsWith("<!DOCTYPE html") || body.startsWith("<html")) {
            throw new YahooAuthException("HTML response received from Yahoo");
        }

        // SEMANTIC ERRORS (INPUT / SYMBOL)
        if (body.contains("\"code\":\"Not Found\"")
                && body.contains("No data found")) {

            throw new YahooInvalidSymbolException(
                    "Invalid or unsupported symbol. Yahoo Finance expects a valid ticker " +
                            "(e.g. AAPL, MSFT, PETR4.SA).");
        }

        // Explicit error payload returned by Yahoo
        // This usually indicates a service-side issue
        if (body.contains("\"error\"") && body.contains("\"code\"")) {
            throw new YahooUnavailableException("Yahoo returned error payload");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     *
     * <p>
     * This class is intended to be used only as a static utility.
     */
    private YahooResponseValidator() {
    }
}
