package com.openfinancedatalib.yahoo.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class YahooCrumbStoreStaticTest {

    @Test
    void shouldReuseCrumbAcrossProviders() {
        YahooSessionManager session1 = new YahooSessionManager();
        YahooCrumbProvider provider1 = new YahooCrumbProvider(session1);

        String crumb1 = provider1.getCrumb();

        YahooSessionManager session2 = new YahooSessionManager();
        YahooCrumbProvider provider2 = new YahooCrumbProvider(session2);

        String crumb2 = provider2.getCrumb();

        System.out.println("Crumb 1: " + crumb1);
        System.out.println("Crumb 2: " + crumb2);

        assertEquals(crumb1, crumb2);
    }
}
