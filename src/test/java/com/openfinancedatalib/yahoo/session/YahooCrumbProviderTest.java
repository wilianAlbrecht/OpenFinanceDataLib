package com.openfinancedatalib.yahoo.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class YahooCrumbProviderTest {

    @Test
    void shouldFetchCrumbWithValidCookies() {
        YahooSessionManager sessionManager = new YahooSessionManager();
        YahooCrumbProvider provider = new YahooCrumbProvider(sessionManager);

        String crumb = provider.fetchCrumb();

        System.out.println("Yahoo Crumb fetched: [" + crumb + "]");

        assertNotNull(crumb, "Crumb should not be null");
        assertFalse(crumb.isBlank(), "Crumb should not be blank");
        assertFalse(
                crumb.contains("Invalid Cookie"),
                "Yahoo rejected crumb due to invalid cookies"
        );
    }
}
