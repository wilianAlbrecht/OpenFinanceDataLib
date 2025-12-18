package com.openfinancedatalib.yahoo;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.client.YahooHistoryClient;
import com.openfinancedatalib.yahoo.client.YahooQuoteClient;
import com.openfinancedatalib.yahoo.client.YahooQuoteSummaryClient;
import com.openfinancedatalib.yahoo.client.YahooSearchClient;
import com.openfinancedatalib.yahoo.enums.YahooApiType;
import com.openfinancedatalib.yahoo.exception.YahooAuthException;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

public class YahooRequestCoordinator {

    private final YahooCrumbProvider crumbProvider;

    private final YahooQuoteSummaryClient quoteSummaryClient;
    private final YahooQuoteClient quoteClient;
    private final YahooHistoryClient historyClient;
    private final YahooSearchClient searchClient;
    private final YahooSessionManager sessionManager;

    public YahooRequestCoordinator() {
        YahooSessionManager sessionManager = new YahooSessionManager();

        this.crumbProvider = new YahooCrumbProvider(sessionManager);
        this.quoteSummaryClient = new YahooQuoteSummaryClient(sessionManager);
        this.quoteClient = new YahooQuoteClient(sessionManager);
        this.historyClient = new YahooHistoryClient(sessionManager);
        this.searchClient = new YahooSearchClient(sessionManager);
        this.sessionManager = sessionManager;
    }

    public JsonNode requestCoordinator(
            String symbol,
            YahooApiType apiType,
            Map<String, String> params) {

        String crumb = getValidCrumb();

        try {

            return dispatch(symbol, apiType, params, crumb);
            
        } catch (YahooAuthException e) {

            YahooCrumbStore.clear();
            sessionManager.getClient();
            String newCrumb = crumbProvider.getCrumb();

            return dispatch(symbol, apiType, params, newCrumb);
        }

    }

    private JsonNode dispatch(
            String symbol,
            YahooApiType apiType,
            Map<String, String> params,
            String crumb) {

        return switch (apiType) {

            case QUOTE_SUMMARY ->
                quoteSummaryClient.request(symbol, params, crumb);

            case QUOTE ->
                quoteClient.request(symbol, params, crumb);

            case HISTORY ->
                historyClient.request(symbol, params, crumb);

            case SEARCH ->
                searchClient.request(params, crumb);

            default ->
                throw new IllegalArgumentException(
                        "Unsupported Yahoo API type: " + apiType);
        };
    }

    private String getValidCrumb() {

        // Modelo antigo: sempre garantir sessão antes do crumb
        sessionManager.getClient(); // força ensureSession()

        if (YahooCrumbStore.isValid()) {
            return YahooCrumbStore.get();
        }

        String crumb = crumbProvider.getCrumb();

        if (crumb == null || crumb.isBlank()) {
            throw new YahooAuthException("Failed to obtain Yahoo crumb");
        }

        return crumb;
    }

}
