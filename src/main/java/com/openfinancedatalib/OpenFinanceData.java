package com.openfinancedatalib;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.YahooRequestCoordinator;

public class OpenFinanceData {

    private final YahooRequestCoordinator requestCoordinator;

    // -------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------
    public OpenFinanceData() {
        this.requestCoordinator = new YahooRequestCoordinator();
    }

    // -------------------------------------------------
    // PUBLIC API
    // -------------------------------------------------
    public JsonNode getFundamentals(String symbol) {
        return requestCoordinator.getFundamentals(symbol);
    }
}
