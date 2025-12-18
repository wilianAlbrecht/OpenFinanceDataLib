package com.openfinancedatalib;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

class OpenFinanceDataTest {

    private final OpenFinanceData openFinanceData = new OpenFinanceData();

    private static final String SYMBOL = "AAPL";

    // =================================================
    // QUOTE SUMMARY — DEFAULT
    // =================================================

    @Test
    @DisplayName("Should fetch default quoteSummary (fundamentals)")
    void testGetFundamentals() {

        JsonNode result = openFinanceData.getFundamentals(SYMBOL);

        assertNotNull(result);
        assertTrue(result.has("quoteSummary"));
    }

    // =================================================
    // QUOTE SUMMARY — MODULES
    // =================================================

    @Test
    @DisplayName("Should fetch profile module")
    void testGetProfile() {

        JsonNode result = openFinanceData.getProfile(SYMBOL);

        assertNotNull(result);
        assertTrue(
                result.at("/quoteSummary/result/0").has("summaryProfile")
        );
    }

    @Test
    @DisplayName("Should fetch earnings modules")
    void testGetEarnings() {

        JsonNode result = openFinanceData.getEarnings(SYMBOL);

        JsonNode root = result.at("/quoteSummary/result/0");

        assertNotNull(root);
        assertTrue(root.has("earnings"));
    }

    @Test
    @DisplayName("Should fetch multiple custom modules")
    void testGetQuoteSummaryCustomModules() {

        JsonNode result = openFinanceData.getQuoteSummary(
                SYMBOL,
                Map.of(
                        "modules",
                        "summaryDetail,financialData,summaryProfile"
                )
        );

        JsonNode root = result.at("/quoteSummary/result/0");

        assertNotNull(root);
        assertTrue(root.has("summaryDetail"));
        assertTrue(root.has("financialData"));
        assertTrue(root.has("summaryProfile"));
    }

    // =================================================
    // QUOTE
    // =================================================

    @Test
    @DisplayName("Should fetch quote data")
    void testGetQuote() {

        JsonNode result = openFinanceData.getQuote(SYMBOL);

        assertNotNull(result);
        assertTrue(result.has("quoteResponse"));
    }

    // =================================================
    // HISTORY
    // =================================================

    @Test
    @DisplayName("Should fetch price history")
    void testGetHistory() {

        JsonNode result = openFinanceData.getHistory(
                SYMBOL,
                "1mo",
                "1d"
        );

        assertNotNull(result);
        assertTrue(result.has("chart"));
    }

    @Test
    @DisplayName("Should fetch price history with dividends")
    void testGetHistoryWithEvents() {

        JsonNode result = openFinanceData.getHistoryWithEvents(
                SYMBOL,
                "6mo",
                "1d",
                "div"
        );

        JsonNode chart = result.at("/chart/result/0");

        assertNotNull(chart);
        assertTrue(chart.has("events"));
    }

    // =================================================
    // SEARCH
    // =================================================

    @Test
    @DisplayName("Should search symbols")
    void testSearch() {

        JsonNode result = openFinanceData.search("Apple");

        assertNotNull(result);
        assertTrue(result.has("quotes"));
    }
}
