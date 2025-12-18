package com.openfinancedatalib.yahoo.session;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class YahooCrumbProvider {

    private static final String CRUMB_URL =
            "https://query1.finance.yahoo.com/v1/test/getcrumb";

    private static final Duration CRUMB_TTL = Duration.ofMinutes(10);

    private final YahooSessionManager sessionManager;

    public YahooCrumbProvider(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public String getCrumb() {
        // Cache global
        String cached = YahooCrumbStore.get();
        if (cached != null) {
            return cached;
        }

        // Criar sessão temporária (cookies vivos)
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CRUMB_URL))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            String crumb = response.body();

            // validação entra no próximo passo

            // Cache de processo
            YahooCrumbStore.put(crumb, CRUMB_TTL);

            return crumb;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Yahoo crumb", e);
        }
    }
}
