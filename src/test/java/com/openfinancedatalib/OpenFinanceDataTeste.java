package com.openfinancedatalib;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

class OpenFinanceDataTest {

    @Test
    void shouldFetchFundamentalsUsingPublicApi() {

        // usuário instancia apenas isso
        OpenFinanceData openFinanceData = new OpenFinanceData();

        // chamada da API pública
        JsonNode result = openFinanceData.getFundamentals("AAPL");

        // debug visual (opcional)
        System.out.println(result.toPrettyString());

        // validações mínimas e seguras
        assertNotNull(result, "Result must not be null");
        assertTrue(
                result.has("quoteSummary"),
                "Response must contain quoteSummary"
        );
        assertTrue(
                result.get("quoteSummary").has("result"),
                "quoteSummary must contain result"
        );
    }
}
