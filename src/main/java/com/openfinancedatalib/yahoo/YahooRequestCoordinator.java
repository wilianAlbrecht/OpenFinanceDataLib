package com.openfinancedatalib.yahoo;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.client.YahooFundamentalsClient;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

public class YahooRequestCoordinator {

    private final YahooCrumbProvider crumbProvider;
    private final YahooFundamentalsClient fundamentalsClient;

    // -------------------------------------------------
    // CONSTRUCTOR (vazio para quem chama)
    // -------------------------------------------------
    public YahooRequestCoordinator() {
        YahooSessionManager sessionManager = new YahooSessionManager();
        this.crumbProvider = new YahooCrumbProvider(sessionManager);
        this.fundamentalsClient = new YahooFundamentalsClient(sessionManager);
    }

    // -------------------------------------------------
    // COORDENAÇÃO
    // -------------------------------------------------
    public JsonNode getFundamentals(String symbol) {

        // tenta obter do cache (variável estática)
        String crumb = YahooCrumbStore.get();

        if (crumb == null) {
            crumb = crumbProvider.getCrumb();
        }

        try {
            return fundamentalsClient.getFundamentals(symbol, crumb);

        } catch (RuntimeException e) {

            // se falhar, invalida cache e tenta uma vez mais
            if (isAuthError(e)) {
                YahooCrumbStore.clear();
                String newCrumb = crumbProvider.getCrumb();
                return fundamentalsClient.getFundamentals(symbol, newCrumb);
            }

            throw e;
        }
    }

    private boolean isAuthError(RuntimeException e) {
        // depois vira exceção específica
        return true;
    }
}
