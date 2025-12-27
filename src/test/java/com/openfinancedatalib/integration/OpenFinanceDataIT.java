package com.openfinancedatalib.integration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.OpenFinanceData;

class OpenFinanceDataIT {

    private static OpenFinanceData api;

    @BeforeAll
    static void setup() {
        api = new OpenFinanceData();
    }

    @Test
    void shouldFetchFundamentals() {
        JsonNode result = api.getFundamentals("AAPL");
        assertNotNull(result);
        assertTrue(result.has("quoteSummary"));
    }

    @Test
    void shouldFetchProfile() {
        JsonNode result = api.getProfile("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchEarnings() {
        JsonNode result = api.getEarnings("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchFinancialStatements() {
        JsonNode result = api.getFinancialStatements("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchAnalystRecommendations() {
        JsonNode result = api.getAnalystRecommendations("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchCalendarEvents() {
        JsonNode result = api.getCalendarEvents("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchOwnership() {
        JsonNode result = api.getOwnership("AAPL");
        assertNotNull(result);
    }

    @Test
    void shouldFetchQuote() {
        JsonNode result = api.getQuote("AAPL");
        System.out.println(result.toPrettyString());
        assertNotNull(result);
    }

    @Test
    void shouldFetchHistory() {
        JsonNode result = api.getHistory("AAPL", "1mo", "1d");
        assertNotNull(result);
    }

    @Test
    void shouldFetchHistoryWithEvents() {
        JsonNode result = api.getHistoryWithEvents(
                "AAPL", "6mo", "1d", "div");
        assertNotNull(result);
    }

    @Test
    void shouldSearchAssets() {
        JsonNode result = api.search("Apple");
        assertNotNull(result);
    }

    // -------------------------------------------------
    // COOL-DOWN BETWEEN TESTS (ANTI RATE-LIMIT)
    // -------------------------------------------------
    @AfterEach
    void cooldown() throws InterruptedException {
        Thread.sleep(1200);
    }
}
