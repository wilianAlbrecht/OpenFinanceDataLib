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
 * Client responsible for executing Yahoo Finance search requests.
 *
 * <p>
 * This client interacts with the Yahoo Finance
 * {@code /v1/finance/search} endpoint, which allows searching
 * for assets using keywords or company names.
 *
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Build the search request URL</li>
 *   <li>Execute the HTTP request</li>
 *   <li>Validate the response</li>
 *   <li>Parse the JSON response</li>
 * </ul>
 *
 * <p>
 * This class does NOT:
 * <ul>
 *   <li>Manage cookies or sessions</li>
 *   <li>Fetch or refresh crumbs</li>
 *   <li>Handle retries</li>
 * </ul>
 *
 * <p>
 * All session handling and retry logic is delegated to
 * higher-level components.
 */
public class YahooSearchClient {

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
     * Creates a new {@code YahooSearchClient}.
     *
     * @param sessionManager session manager responsible for cookies
     */
    public YahooSearchClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Executes a search request against Yahoo Finance.
     *
     * <p>
     * Required parameters:
     * <ul>
     *   <li>{@code query} – search term (company name or keyword)</li>
     * </ul>
     *
     * <p>
     * Optional parameters:
     * <ul>
     *   <li>{@code quotesCount} – number of quote results (default: 10)</li>
     *   <li>{@code newsCount} – number of news results (default: 0)</li>
     * </ul>
     *
     * <p>
     * The crumb parameter is required by Yahoo Finance for authorization
     * and must be provided by the caller.
     *
     * @param params map containing search parameters
     * @param crumb valid Yahoo crumb
     * @return {@link JsonNode} containing the search results
     *
     * @throws IllegalArgumentException if the search query is missing
     * @throws RuntimeException if the request fails or the response
     *         cannot be parsed
     */
    public JsonNode request(Map<String, String> params, String crumb) {
        try {
            // Required parameter: search query
            String query = params.get("query");

            if (query == null || query.isBlank()) {
                throw new IllegalArgumentException("Search query is required");
            }

            // Optional parameters with default values
            String quotesCount = params.getOrDefault("quotesCount", "10");
            String newsCount = params.getOrDefault("newsCount", "0");

            // Build Yahoo Finance search URL
            String url =
                    "https://query1.finance.yahoo.com/v1/finance/search"
                            + "?q=" + query
                            + "&quotesCount=" + quotesCount
                            + "&newsCount=" + newsCount
                            + "&crumb=" + crumb;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
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
            throw new RuntimeException("Failed to search Yahoo", e);
        }
    }
}
