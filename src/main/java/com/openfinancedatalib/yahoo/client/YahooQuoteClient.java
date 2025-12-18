package com.openfinancedatalib.yahoo.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;
import com.openfinancedatalib.yahoo.validator.YahooResponseValidator;

public class YahooQuoteClient {

    private final YahooSessionManager sessionManager;
    private final ObjectMapper mapper = new ObjectMapper();

    public YahooQuoteClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public JsonNode request(String symbol, Map<String, String> params, String crumb) {
        try {
            String url =
                    "https://query1.finance.yahoo.com/v7/finance/quote"
                            + "?symbols=" + symbol
                            + "&crumb=" + crumb;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            HttpResponse<String> response =
                    sessionManager.getClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            YahooResponseValidator.validate(
                    response.statusCode(),
                    response.body()
            );

            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Yahoo quote for " + symbol, e);
        }
    }
}
