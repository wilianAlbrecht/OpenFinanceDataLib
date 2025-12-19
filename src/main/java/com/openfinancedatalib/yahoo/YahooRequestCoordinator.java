package com.openfinancedatalib.yahoo;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.client.YahooHistoryClient;
import com.openfinancedatalib.yahoo.client.YahooQuoteClient;
import com.openfinancedatalib.yahoo.client.YahooQuoteSummaryClient;
import com.openfinancedatalib.yahoo.client.YahooSearchClient;
import com.openfinancedatalib.yahoo.enums.YahooApiType;
import com.openfinancedatalib.yahoo.exception.YahooAuthException;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

/**
 * Central coordinator for all Yahoo Finance requests.
 *
 * <p>
 * This class is the core of the Yahoo integration and is responsible for
 * orchestrating the complete request lifecycle, including:
 * <ul>
 *   <li>Ensuring Yahoo cookies are captured</li>
 *   <li>Obtaining a valid crumb</li>
 *   <li>Dispatching the request to the correct API client</li>
 *   <li>Handling authentication failures and retrying requests</li>
 * </ul>
 *
 * <p>
 * All Yahoo-specific complexity is intentionally centralized here to keep
 * the public API clean and easy to use.
 */
public class YahooRequestCoordinator {

    /**
     * Provider responsible for fetching Yahoo crumbs.
     * <p>
     * Assumes cookies are already valid.
     */
    private final YahooCrumbProvider crumbProvider;

    /** Client for the quoteSummary endpoint */
    private final YahooQuoteSummaryClient quoteSummaryClient;

    /** Client for the quote (price) endpoint */
    private final YahooQuoteClient quoteClient;

    /** Client for the historical price (chart) endpoint */
    private final YahooHistoryClient historyClient;

    /** Client for the search endpoint */
    private final YahooSearchClient searchClient;

    /**
     * Manages Yahoo HTTP session and cookies.
     */
    private final YahooSessionManager sessionManager;

    /**
     * Creates a new {@code YahooRequestCoordinator} and initializes
     * all internal components.
     *
     * <p>
     * A single {@link YahooSessionManager} instance is shared across
     * all clients to ensure cookies are consistent.
     */
    public YahooRequestCoordinator() {
        YahooSessionManager sessionManager = new YahooSessionManager();

        this.crumbProvider = new YahooCrumbProvider(sessionManager);
        this.quoteSummaryClient = new YahooQuoteSummaryClient(sessionManager);
        this.quoteClient = new YahooQuoteClient(sessionManager);
        this.historyClient = new YahooHistoryClient(sessionManager);
        this.searchClient = new YahooSearchClient(sessionManager);
        this.sessionManager = sessionManager;
    }

    /**
     * Main entry point for executing a Yahoo Finance request.
     *
     * <p>
     * Execution flow:
     * <ol>
     *   <li>Ensure a valid session and crumb</li>
     *   <li>Dispatch the request to the correct client</li>
     *   <li>If authentication fails, renew crumb and retry once</li>
     * </ol>
     *
     * <p>
     * This method retries <b>at most once</b> to avoid infinite loops.
     *
     * @param symbol asset ticker symbol (may be {@code null} for SEARCH)
     * @param apiType type of Yahoo API to call
     * @param params query parameters for the request
     * @return Yahoo response as a {@link JsonNode}
     */
    public JsonNode requestCoordinator(
            String symbol,
            YahooApiType apiType,
            Map<String, String> params) {

        // Obtain a valid crumb before dispatching the request
        String crumb = getValidCrumb();

        try {
            // Execute the request using the current crumb
            return dispatch(symbol, apiType, params, crumb);

        } catch (YahooAuthException e) {
            // Authentication failed (expired crumb or session)

            // Clear cached crumb to force regeneration
            YahooCrumbStore.clear();

            // Re-initialize session (recapture cookies)
            sessionManager.getClient();

            // Obtain a new crumb and retry the request once
            String newCrumb = crumbProvider.getCrumb();

            return dispatch(symbol, apiType, params, newCrumb);
        }
    }

    /**
     * Dispatches the request to the appropriate Yahoo client
     * based on the API type.
     *
     * @param symbol asset ticker symbol
     * @param apiType Yahoo API type
     * @param params query parameters
     * @param crumb valid Yahoo crumb
     * @return Yahoo response as {@link JsonNode}
     */
    private JsonNode dispatch(
            String symbol,
            YahooApiType apiType,
            Map<String, String> params,
            String crumb) {

        return switch (apiType) {

            case QUOTE_SUMMARY ->
                quoteSummaryClient.request(symbol, params, crumb);

            case QUOTE ->
                quoteClient.request(symbol, params, crumb);

            case HISTORY ->
                historyClient.request(symbol, params, crumb);

            case SEARCH ->
                searchClient.request(params, crumb);

            default ->
                throw new IllegalArgumentException(
                        "Unsupported Yahoo API type: " + apiType);
        };
    }

    /**
     * Returns a valid Yahoo crumb, ensuring that cookies
     * are captured beforehand.
     *
     * <p>
     * This method follows the legacy Yahoo flow:
     * <ol>
     *   <li>Ensure cookies are available</li>
     *   <li>Reuse cached crumb if still valid</li>
     *   <li>Fetch a new crumb if necessary</li>
     * </ol>
     *
     * @return a valid Yahoo crumb
     * @throws YahooAuthException if a crumb cannot be obtained
     */
    private String getValidCrumb() {

        // Always ensure a valid session before requesting a crumb
        sessionManager.getClient(); // forces ensureSession()

        // Reuse cached crumb if still valid
        if (YahooCrumbStore.isValid()) {
            return YahooCrumbStore.get();
        }

        // Fetch a new crumb
        String crumb = crumbProvider.getCrumb();

        // Defensive validation
        if (crumb == null || crumb.isBlank()) {
            throw new YahooAuthException("Failed to obtain Yahoo crumb");
        }

        return crumb;
    }
}
