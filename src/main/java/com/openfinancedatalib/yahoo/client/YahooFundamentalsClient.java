package com.openfinancedatalib.yahoo.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;
import com.openfinancedatalib.yahoo.url.YahooUrlBuilder;

public class YahooFundamentalsClient {

    private static final List<String> DEFAULT_MODULES = List.of(
            "summaryDetail",
            "defaultKeyStatistics",
            "financialData"
    );

    private final YahooSessionManager sessionManager;
    private final ObjectMapper mapper = new ObjectMapper();

    public YahooFundamentalsClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Executa a operação usando um crumb já validado.
     */
    public JsonNode getFundamentals(String symbol, String crumb) {
        try {
            String url = YahooUrlBuilder.fundamentals(
                    symbol,
                    DEFAULT_MODULES,
                    crumb
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch Yahoo fundamentals for " + symbol, e);
        }
    }
}
