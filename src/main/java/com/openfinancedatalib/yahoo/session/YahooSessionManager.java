package com.openfinancedatalib.yahoo.session;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

public class YahooSessionManager {

    private static final String SESSION_INIT_URL = "https://fc.yahoo.com";

    private final CookieManager cookieManager;
    private final HttpClient client;
    private boolean initialized = false;

    public YahooSessionManager() {
        this.cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

        this.client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public HttpClient getClient() {
        ensureSession();
        return client;
    }

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

            client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());

            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Yahoo session", e);
        }
    }
}
