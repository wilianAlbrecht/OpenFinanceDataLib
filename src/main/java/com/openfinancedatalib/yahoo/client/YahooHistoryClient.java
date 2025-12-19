package com.openfinancedatalib.yahoo.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;
import com.openfinancedatalib.yahoo.validator.YahooResponseValidator;

/**
 * Client responsible for retrieving historical price data
 * from Yahoo Finance.
 *
 * <p>
 * This client interacts with the Yahoo Finance
 * {@code /v8/finance/chart} endpoint, which provides
 * time-series market data such as:
 * <ul>
 *   <li>Open, high, low, close prices</li>
 *   <li>Volume</li>
 *   <li>Adjusted close prices</li>
 *   <li>Corporate events (dividends, splits)</li>
 * </ul>
 *
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Build the historical data request URL</li>
 *   <li>Apply default values for range and interval</li>
 *   <li>Execute the HTTP request</li>
 *   <li>Validate the response</li>
 *   <li>Parse the JSON payload</li>
 * </ul>
 *
 * <p>
 * This class does NOT:
 * <ul>
 *   <li>Manage cookies or session lifecycle</li>
 *   <li>Fetch or refresh crumbs</li>
 *   <li>Handle retries or fallback logic</li>
 * </ul>
 *
 * <p>
 * All authentication, retry, and session recovery logic
 * is handled by higher-level components.
 */
public class YahooHistoryClient {

    /**
     * Session manager providing an {@link java.net.http.HttpClient}
     * with valid Yahoo cookies.
     */
    private final YahooSessionManager sessionManager;

    /**
     * Jackson object mapper used to parse JSON responses.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a new {@code YahooHistoryClient}.
     *
     * @param sessionManager session manager responsible for cookies
     */
    public YahooHistoryClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Executes a historical price request for the given symbol.
     *
     * <p>
     * Supported parameters:
     * <ul>
     *   <li>{@code range} – time range (default: {@code 1mo})</li>
     *   <li>{@code interval} – data interval (default: {@code 1d})</li>
     *   <li>{@code events} – optional corporate events
     *       ({@code div}, {@code splits}, {@code div,splits})</li>
     * </ul>
     *
     * <p>
     * Examples:
     * <ul>
     *   <li>range: {@code 1mo}, {@code 6mo}, {@code 1y}, {@code 5y}</li>
     *   <li>interval: {@code 1d}, {@code 1wk}, {@code 1mo}</li>
     * </ul>
     *
     * <p>
     * The crumb parameter is required by Yahoo Finance
     * for request authorization and must be provided
     * by the caller.
     *
     * @param symbol asset ticker symbol (e.g. AAPL, MSFT)
     * @param params query parameters controlling range, interval and events
     * @param crumb valid Yahoo crumb
     * @return {@link JsonNode} containing historical price data
     *
     * @throws RuntimeException if the request fails or
     *         the response cannot be parsed
     */
    public JsonNode request(String symbol, Map<String, String> params, String crumb) {
        try {
            // Resolve parameters with default values
            String range = params.getOrDefault("range", "1mo");
            String interval = params.getOrDefault("interval", "1d");
            String events = params.get("events");

            // Build Yahoo Finance chart URL
            StringBuilder url = new StringBuilder(
                    "https://query1.finance.yahoo.com/v8/finance/chart/"
                            + symbol
                            + "?range=" + range
                            + "&interval=" + interval
                            + "&crumb=" + crumb
            );

            // Optional events (dividends, splits)
            if (events != null) {
                url.append("&events=").append(events);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            // Execute request using an HttpClient with valid cookies
            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            // Validate HTTP status and response body
            YahooResponseValidator.validate(
                    response.statusCode(),
                    response.body()
            );

            // Parse and return JSON response
            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch Yahoo history for " + symbol, e);
        }
    }
}
