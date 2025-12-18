package com.openfinancedatalib.yahoo;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.client.YahooFundamentalsClient;
import com.openfinancedatalib.yahoo.exception.YahooAuthException;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

public class YahooRequestCoordinator {

    private final YahooCrumbProvider crumbProvider;
    private final YahooFundamentalsClient fundamentalsClient;

    public YahooRequestCoordinator() {
        YahooSessionManager sessionManager = new YahooSessionManager();
        this.crumbProvider = new YahooCrumbProvider(sessionManager);
        this.fundamentalsClient = new YahooFundamentalsClient(sessionManager);
    }

    public JsonNode getFundamentals(String symbol) {

        // Validação PREVENTIVA do crumb
        String crumb;
        if (!YahooCrumbStore.isExpired()) {
            crumb = YahooCrumbStore.get();
        } else {
            crumb = crumbProvider.getCrumb();
        }

        // Execução normal
        try {
            return fundamentalsClient.getFundamentals(symbol, crumb);

        } catch (YahooAuthException e) {
            // Fallback EXCEPCIONAL
            YahooCrumbStore.clear();

            String newCrumb = crumbProvider.getCrumb();
            return fundamentalsClient.getFundamentals(symbol, newCrumb);
        }
    }
}
