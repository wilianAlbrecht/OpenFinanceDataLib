package com.openfinancedatalib.yahoo.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

class YahooFundamentalsClientTest {

    @Test
    void shouldFetchFundamentalsWithCrumb() {
        YahooCrumbStore.clear();

        YahooSessionManager sessionManager = new YahooSessionManager();
        YahooCrumbProvider crumbProvider = new YahooCrumbProvider(sessionManager);

        String crumb = crumbProvider.getCrumb();

        YahooFundamentalsClient client =
                new YahooFundamentalsClient(sessionManager);

        JsonNode result = client.getFundamentals("AAPL", crumb);

        System.out.println(result.toPrettyString());

        assertNotNull(result);
        assertTrue(result.has("quoteSummary"));
    }
}
