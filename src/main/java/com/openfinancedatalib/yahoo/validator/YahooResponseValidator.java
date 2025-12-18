package com.openfinancedatalib.yahoo.validator;

import com.openfinancedatalib.yahoo.exception.YahooAuthException;
import com.openfinancedatalib.yahoo.exception.YahooRateLimitException;
import com.openfinancedatalib.yahoo.exception.YahooUnavailableException;

public class YahooResponseValidator {

    public static void validate(int statusCode, String body) {

        // ----------------------------------
        // HTTP STATUS
        // ----------------------------------
        if (statusCode == 401 || statusCode == 403) {
            throw new YahooAuthException("Unauthorized request to Yahoo");
        }

        if (statusCode == 429) {
            throw new YahooRateLimitException("Yahoo rate limit exceeded");
        }

        if (statusCode >= 500) {
            throw new YahooUnavailableException(
                    "Yahoo service unavailable (status " + statusCode + ")"
            );
        }

        // ----------------------------------
        // BODY CONTENT
        // ----------------------------------
        if (body == null || body.isBlank()) {
            throw new YahooUnavailableException("Empty response from Yahoo");
        }

        // HTML response (Yahoo sometimes does this)
        if (body.startsWith("<!DOCTYPE html") || body.startsWith("<html")) {
            throw new YahooAuthException("HTML response received from Yahoo");
        }

        // Erro explícito do Yahoo
        if (body.contains("\"error\"") && body.contains("\"code\"")) {
            throw new YahooUnavailableException("Yahoo returned error payload");
        }
    }

    private YahooResponseValidator() {
    }
}
