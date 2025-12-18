package com.openfinancedatalib.yahoo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.OpenFinanceData;

class YahooFinanceClientTest {

    @Test
    void shouldFetchFundamentalsViaFacade() {
        OpenFinanceData yahoo = new OpenFinanceData();

        JsonNode fundamentals = yahoo.getFundamentals("AAPL");

        System.out.println(fundamentals.toPrettyString());

        assertNotNull(fundamentals);
        assertTrue(fundamentals.has("quoteSummary"));
    }
}
