package com.openfinancedatalib.yahoo.session;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YahooCrumbProvider {

    private static final String CRUMB_URL =
            "https://query1.finance.yahoo.com/v1/test/getcrumb";

    private final YahooSessionManager sessionManager;

    public YahooCrumbProvider(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public String fetchCrumb() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CRUMB_URL))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Yahoo crumb", e);
        }
    }
}
