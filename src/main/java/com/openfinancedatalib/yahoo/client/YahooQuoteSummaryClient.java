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

public class YahooQuoteSummaryClient {

        private static final String BASE_URL = "https://query1.finance.yahoo.com/v10/finance/quoteSummary/";

        private static final List<String> DEFAULT_MODULES = List.of(
                        "summaryDetail",
                        "defaultKeyStatistics",
                        "financialData");

        private final YahooSessionManager sessionManager;
        private final ObjectMapper mapper = new ObjectMapper();

        public YahooQuoteSummaryClient(YahooSessionManager sessionManager) {
                this.sessionManager = sessionManager;
        }

        public JsonNode request(
                        String symbol,
                        Map<String, String> params,
                        String crumb) {
                try {

                        if (YahooCrumbStore.isValid()) {
                                crumb = YahooCrumbStore.get();
                        } else {
                                YahooCrumbProvider provider = new YahooCrumbProvider(sessionManager);
                                crumb = provider.getCrumb();
                        }

                        String modules = resolveModules(params);

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

                        HttpResponse<String> response = sessionManager.getClient()
                                        .send(builder.build(), HttpResponse.BodyHandlers.ofString());

                        YahooResponseValidator.validate(
                                        response.statusCode(),
                                        response.body());

                        return mapper.readTree(response.body());

                } catch (Exception e) {
                        throw new RuntimeException(
                                        "Failed to fetch Yahoo quoteSummary for " + symbol, e);
                }
        }

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
