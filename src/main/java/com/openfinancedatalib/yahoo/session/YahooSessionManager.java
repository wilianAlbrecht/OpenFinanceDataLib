package com.openfinancedatalib.yahoo.session;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

/**
 * Manages the Yahoo Finance HTTP session and cookies.
 *
 * <p>
 * This class is responsible <b>exclusively</b> for:
 * <ul>
 *   <li>Capturing Yahoo cookies</li>
 *   <li>Maintaining a valid HTTP session</li>
 * </ul>
 *
 * <p>
 * It does <b>not</b>:
 * <ul>
 *   <li>Fetch crumb tokens</li>
 *   <li>Execute Yahoo API requests</li>
 *   <li>Handle retries or errors</li>
 * </ul>
 *
 * <p>
 * The session is initialized by performing a simple GET request
 * to {@code https://fc.yahoo.com}. This step is required by Yahoo
 * before a valid crumb can be obtained.
 *
 * <p>
 * The response body is ignored; only the cookies returned in
 * the HTTP headers are relevant.
 */
public class YahooSessionManager {

    /**
     * URL used only to initialize the Yahoo session and capture cookies.
     */
    private static final String SESSION_INIT_URL = "https://fc.yahoo.com";

    /**
     * Cookie manager that stores all cookies returned by Yahoo.
     * <p>
     * Uses {@link CookiePolicy#ACCEPT_ALL} because Yahoo may return
     * multiple cookies required for authentication.
     */
    private final CookieManager cookieManager;

    /**
     * HTTP client configured with a {@link CookieManager}.
     * <p>
     * This client instance is reused across all requests
     * to ensure cookies remain consistent.
     */
    private final HttpClient client;

    /**
     * Indicates whether the Yahoo session has already been initialized.
     * <p>
     * When {@code true}, cookies are assumed to be present and valid.
     */
    private boolean initialized = false;

    /**
     * Creates a new {@code YahooSessionManager}.
     *
     * <p>
     * Initializes an {@link HttpClient} with:
     * <ul>
     *   <li>Automatic cookie handling</li>
     *   <li>Redirect following enabled</li>
     *   <li>A reasonable connection timeout</li>
     * </ul>
     */
    public YahooSessionManager() {
        this.cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

        this.client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Returns an {@link HttpClient} with a valid Yahoo session.
     *
     * <p>
     * If the session has not been initialized yet, this method
     * triggers a request to {@link #SESSION_INIT_URL} in order
     * to capture the required cookies.
     *
     * <p>
     * This method is safe to call multiple times.
     *
     * @return an {@link HttpClient} with Yahoo cookies attached
     */
    public HttpClient getClient() {
        ensureSession();
        return client;
    }

    /**
     * Ensures that the Yahoo session has been initialized.
     *
     * <p>
     * This method performs a single HTTP request to
     * {@link #SESSION_INIT_URL} to capture cookies.
     *
     * <p>
     * The request body is discarded because only the
     * response headers (cookies) are relevant.
     *
     * <p>
     * This method is idempotent: once the session is initialized,
     * subsequent calls will have no effect.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void ensureSession() {
        if (initialized) {
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SESSION_INIT_URL))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            // The response body is irrelevant; cookies are captured via headers
            client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());

            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Yahoo session", e);
        }
    }
}
