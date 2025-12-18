package com.openfinancedatalib.yahoo.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;
import com.openfinancedatalib.yahoo.validator.YahooResponseValidator;

public class YahooHistoryClient {

    private final YahooSessionManager sessionManager;
    private final ObjectMapper mapper = new ObjectMapper();

    public YahooHistoryClient(YahooSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public JsonNode request(String symbol, Map<String, String> params, String crumb) {
        try {
            String range = params.getOrDefault("range", "1mo");
            String interval = params.getOrDefault("interval", "1d");
            String events = params.get("events");

            StringBuilder url = new StringBuilder(
                    "https://query1.finance.yahoo.com/v8/finance/chart/"
                            + symbol
                            + "?range=" + range
                            + "&interval=" + interval
                            + "&crumb=" + crumb
            );

            if (events != null) {
                url.append("&events=").append(events);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
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
            throw new RuntimeException(
                    "Failed to fetch Yahoo history for " + symbol, e);
        }
    }
}
