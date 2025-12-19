package com.openfinancedatalib.yahoo.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;
import com.openfinancedatalib.yahoo.validator.YahooResponseValidator;

/**
 * Client responsible for calling the Yahoo Finance {@code quoteSummary} endpoint.
 *
 * <p>
 * The {@code quoteSummary} endpoint is one of the most powerful Yahoo Finance APIs
 * and provides access to multiple data modules such as:
 * <ul>
 *   <li>Company fundamentals</li>
 *   <li>Key statistics</li>
 *   <li>Financial statements</li>
 *   <li>Earnings data</li>
 *   <li>Analyst recommendations</li>
 * </ul>
 *
 * <p>
 * This client is responsible for:
 * <ul>
 *   <li>Resolving which modules should be requested</li>
 *   <li>Building the request URL</li>
 *   <li>Executing the HTTP request</li>
 *   <li>Validating the response</li>
 *   <li>Parsing the JSON payload</li>
 * </ul>
 *
 * <p>
 * This class does NOT:
 * <ul>
 *   <li>Control request retries</li>
 *   <li>Manage session lifecycle</li>
 *   <li>Decide when a crumb should be cleared</li>
 * </ul>
 *
 * <p>
 * Retry logic and session recovery are handled by
 * {@link com.openfinancedatalib.yahoo.YahooRequestCoordinator}.
 */
public class YahooQuoteSummaryClient {

        /**
         * Base URL for the Yahoo Finance quoteSummary endpoint.
         */
        private static final String BASE_URL =
                        "https://query1.finance.yahoo.com/v10/finance/quoteSummary/";

        /**
         * Default modules returned when no explicit modules are provided.
         *
         * <p>
         * These modules represent a general financial overview of a company
         * and are used by high-level methods such as {@code getFundamentals()}.
         */
        private static final List<String> DEFAULT_MODULES = List.of(
                        "summaryDetail",
                        "defaultKeyStatistics",
                        "financialData");

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
         * Creates a new {@code YahooQuoteSummaryClient}.
         *
         * @param sessionManager session manager responsible for cookies
         */
        public YahooQuoteSummaryClient(YahooSessionManager sessionManager) {
                this.sessionManager = sessionManager;
        }

        /**
         * Executes a quoteSummary request for a given symbol.
         *
         * <p>
         * Flow:
         * <ol>
         *   <li>Ensure a valid crumb is available</li>
         *   <li>Resolve requested modules</li>
         *   <li>Build the quoteSummary request URL</li>
         *   <li>Execute the HTTP request</li>
         *   <li>Validate and parse the response</li>
         * </ol>
         *
         * <p>
         * The crumb parameter is provided by the caller, but this method
         * defensively reuses a cached crumb if one is still valid.
         *
         * @param symbol asset ticker symbol (e.g. AAPL, MSFT)
         * @param params query parameters, including optional {@code modules}
         * @param crumb Yahoo crumb provided by the coordinator
         * @return {@link JsonNode} containing the quoteSummary response
         *
         * @throws RuntimeException if the request fails or the response
         *         cannot be parsed
         */
        public JsonNode request(
                        String symbol,
                        Map<String, String> params,
                        String crumb) {
                try {

                        // Defensive crumb handling:
                        // reuse cached crumb if still valid, otherwise fetch a new one
                        if (YahooCrumbStore.isValid()) {
                                crumb = YahooCrumbStore.get();
                        } else {
                                YahooCrumbProvider provider =
                                                new YahooCrumbProvider(sessionManager);
                                crumb = provider.getCrumb();
                        }

                        // Resolve which quoteSummary modules should be requested
                        String modules = resolveModules(params);

                        // Build quoteSummary request URL
                        String url = BASE_URL
                                        + symbol
                                        + "?modules=" + modules
                                        + "&crumb=" + crumb
                                        + "&corsDomain=finance.yahoo.com";

                        HttpRequest.Builder builder = HttpRequest.newBuilder()
                                        .uri(URI.create(url))
                                        .GET()
                                        .header("User-Agent", "Mozilla/5.0")
                                        .header("Accept", "application/json")
                                        .header("Referer", "https://finance.yahoo.com/")
                                        .header("Origin", "https://finance.yahoo.com");

                        // Execute request using an HttpClient with valid cookies
                        HttpResponse<String> response = sessionManager.getClient()
                                        .send(builder.build(), HttpResponse.BodyHandlers.ofString());

                        // Validate HTTP status and response body
                        YahooResponseValidator.validate(
                                        response.statusCode(),
                                        response.body());

                        // Parse and return JSON response
                        return mapper.readTree(response.body());

                } catch (Exception e) {
                        throw new RuntimeException(
                                        "Failed to fetch Yahoo quoteSummary for " + symbol, e);
                }
        }

        /**
         * Resolves which quoteSummary modules should be requested.
         *
         * <p>
         * If no modules are provided, a default set of modules
         * is used to ensure meaningful output.
         *
         * <p>
         * Any whitespace in the module list is removed to comply
         * with Yahoo API requirements.
         *
         * @param params request parameters
         * @return comma-separated list of modules
         */
        private String resolveModules(Map<String, String> params) {

                if (params == null) {
                        return String.join(",", DEFAULT_MODULES);
                }

                String modulesParam = params.get("modules");

                if (modulesParam == null || modulesParam.isBlank()) {
                        return String.join(",", DEFAULT_MODULES);
                }

                return modulesParam.replace(" ", "");
        }
}
