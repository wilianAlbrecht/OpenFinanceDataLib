package com.openfinancedatalib.yahoo.url;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class YahooUrlBuilder {

    private static final String BASE_QUERY_URL =
            "https://query1.finance.yahoo.com";

    private YahooUrlBuilder() {}

    // -------------------------------------------------
    // FUNDAMENTALS (quoteSummary)
    // -------------------------------------------------
    public static String fundamentals(String symbol,
                                      List<String> modules,
                                      String crumb) {

        String encodedSymbol = encode(symbol);
        String joinedModules = encode(String.join(",", modules));
        String encodedCrumb = encode(crumb);

        return BASE_QUERY_URL +
                "/v10/finance/quoteSummary/" + encodedSymbol +
                "?modules=" + joinedModules +
                "&crumb=" + encodedCrumb;
    }

    // -------------------------------------------------
    // Helpers
    // -------------------------------------------------
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
