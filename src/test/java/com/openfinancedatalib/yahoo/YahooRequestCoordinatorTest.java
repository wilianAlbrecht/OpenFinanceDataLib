package com.openfinancedatalib.yahoo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.client.YahooFundamentalsClient;
import com.openfinancedatalib.yahoo.session.YahooCrumbProvider;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;
import com.openfinancedatalib.yahoo.session.YahooSessionManager;

class YahooRequestCoordinatorTest {

    @Test
    void shouldReuseCachedCrumbAcrossCalls() {
        YahooCrumbStore.clear();

        YahooSessionManager sessionManager = new YahooSessionManager();
        YahooCrumbProvider crumbProvider = new YahooCrumbProvider(sessionManager);
        YahooFundamentalsClient client =
                new YahooFundamentalsClient(sessionManager);

        YahooRequestCoordinator coordinator =
                new YahooRequestCoordinator();

        JsonNode first = coordinator.getFundamentals("AAPL");
        JsonNode second = coordinator.getFundamentals("AAPL");

        System.out.println("First call done");
        System.out.println("Second call done");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.get("quoteSummary").has("result"),
                     second.get("quoteSummary").has("result"));
    }
}
