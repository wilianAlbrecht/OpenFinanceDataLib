package com.openfinancedatalib.yahoo.session;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Responsible for fetching the Yahoo Finance crumb.
 *
 * <p>
 * A crumb is a short-lived token required by Yahoo Finance
 * to authorize most API requests.
 *
 * <p>
 * This class has a very narrow responsibility:
 * <ul>
 *   <li>Request a crumb from Yahoo Finance</li>
 *   <li>Store it in {@link YahooCrumbStore} with a TTL</li>
 * </ul>
 *
 * <p>
 * IMPORTANT DESIGN NOTES:
 * <ul>
 *   <li>This class assumes that cookies are already valid</li>
 *   <li>It does NOT manage cookies</li>
 *   <li>It does NOT handle retries</li>
 *   <li>It does NOT interpret authentication errors</li>
 * </ul>
 *
 * <p>
 * Cookie management and retry logic are handled by
 * {@link com.openfinancedatalib.yahoo.YahooRequestCoordinator}.
 */
public class YahooCrumbProvider {

    /**
     * Yahoo Finance endpoint used to retrieve a crumb.
     */
    private static final String CRUMB_URL =
            "https://query1.finance.yahoo.com/v1/test/getcrumb";

    /**
     * Time-to-live for the cached crumb.
     * <p>
     * After this duration, the crumb is considered expired
     * and must be fetched again.
     */
    private static final Duration CRUMB_TTL = Duration.ofMinutes(10);

    /**
     * Session manager responsible for providing an {@link java.net.http.HttpClient}
     * with valid Yahoo cookies.
     */
    private final YahooSessionManager sessionManager;

    /**
     * Creates a new {@code YahooCrumbProvider}.
     *
     * @param sessionManager the session manager responsible for cookies
     */
    public YahooCrumbProvider(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Returns a valid Yahoo crumb.
     *
     * <p>
     * Flow:
     * <ol>
     *   <li>Check if a valid crumb exists in {@link YahooCrumbStore}</li>
     *   <li>If not, request a new crumb from Yahoo Finance</li>
     *   <li>Store the crumb in memory with a TTL</li>
     * </ol>
     *
     * <p>
     * This method assumes that a valid Yahoo session
     * (cookies) already exists.
     *
     * @return a valid Yahoo crumb
     *
     * @throws RuntimeException if the crumb cannot be fetched
     */
    public String getCrumb() {

        // Global in-memory cache (JVM-wide)
        String cached = YahooCrumbStore.get();
        if (cached != null) {
            return cached;
        }

        try {
            // Request crumb using an HttpClient with valid cookies
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CRUMB_URL))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            String crumb = response.body();

            // Note:
            // Response validation is handled at a higher level.
            // This class only retrieves and stores the crumb.

            // Store crumb in process-wide cache with TTL
            YahooCrumbStore.put(crumb, CRUMB_TTL);

            return crumb;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Yahoo crumb", e);
        }
    }
}
