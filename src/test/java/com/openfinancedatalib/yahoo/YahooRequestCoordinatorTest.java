package com.openfinancedatalib.yahoo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.enums.YahooApiType;
import com.openfinancedatalib.yahoo.session.YahooCrumbStore;

class YahooRequestCoordinatorTest {

    @Test
    void shouldReuseCachedCrumbAcrossCalls() {
        YahooCrumbStore.clear();

        YahooRequestCoordinator coordinator =
                new YahooRequestCoordinator();

        JsonNode first = coordinator.requestCoordinator("AAPL", YahooApiType.QUOTE_SUMMARY, Map.of());

        System.out.println("First call done");
        System.out.println("Second call done");

        assertNotNull(first);
    }
}
