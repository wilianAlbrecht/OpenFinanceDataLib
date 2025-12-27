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
 * Client responsible for retrieving real-time and delayed
 * quote (price) data from Yahoo Finance.
 *
 * <p>
 * This client interacts with the Yahoo Finance
 * {@code /v7/finance/quote} endpoint, which provides
 * current market data such as:
 * <ul>
 * <li>Last price</li>
 * <li>Price change and percentage</li>
 * <li>Volume</li>
 * <li>Market capitalization</li>
 * </ul>
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Build the quote request URL</li>
 * <li>Execute the HTTP request</li>
 * <li>Validate the response</li>
 * <li>Parse the JSON payload</li>
 * </ul>
 *
 * <p>
 * This class does NOT:
 * <ul>
 * <li>Manage cookies or session lifecycle</li>
 * <li>Fetch or refresh crumbs</li>
 * <li>Handle retries or fallback logic</li>
 * </ul>
 *
 * <p>
 * All authentication, retry, and session recovery logic
 * is handled by higher-level components.
 */
public class YahooQuoteClient {

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
     * Creates a new {@code YahooQuoteClient}.
     *
     * @param sessionManager session manager responsible for cookies
     */
    public YahooQuoteClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Executes a quote request for the given symbol.
     *
     * <p>
     * This method retrieves the current market quote
     * for a single asset.
     *
     * <p>
     * The crumb parameter is required by Yahoo Finance
     * for request authorization and must be provided
     * by the caller.
     *
     * @param symbol asset ticker symbol (e.g. AAPL, MSFT)
     * @param params optional parameters (currently unused)
     * @param crumb  valid Yahoo crumb
     * @return {@link JsonNode} containing quote data
     *
     * @throws RuntimeException if the request fails or
     *                          the response cannot be parsed
     */
    public JsonNode request(String symbol, Map<String, String> params, String crumb) {
        try {
            // Build Yahoo Finance quote URL
            String url = "https://query1.finance.yahoo.com/v7/finance/quote"
                    + "?symbols=" + symbol
                    + "&crumb=" + crumb;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .header("Accept-Encoding", "identity")
                    .build();

            // Execute request using an HttpClient with valid cookies
            HttpResponse<String> response = sessionManager.getClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Validate HTTP status and response body
            YahooResponseValidator.validate(
                    response.statusCode(),
                    response.body());

            // Parse and return JSON response
            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch Yahoo quote for " + symbol, e);
        }
    }
}
