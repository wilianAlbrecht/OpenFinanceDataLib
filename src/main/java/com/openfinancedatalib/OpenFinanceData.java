package com.openfinancedatalib;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.openfinancedatalib.yahoo.YahooRequestCoordinator;
import com.openfinancedatalib.yahoo.enums.YahooApiType;

/**
 * Main entry point of the OpenFinanceData library.
 * <p>
 * This class provides high-level methods to access public financial data
 * from Yahoo Finance, including:
 * <ul>
 *   <li>Company fundamentals</li>
 *   <li>Real-time and delayed quotes</li>
 *   <li>Historical price data</li>
 *   <li>Earnings and financial statements</li>
 *   <li>Analyst recommendations</li>
 *   <li>Asset search</li>
 * </ul>
 *
 * <p>
 * The library user does <b>not</b> need to handle authentication,
 * cookies, sessions, or Yahoo-specific technical details.
 * All these aspects are managed internally by the library.
 *
 * <p>
 * All methods return {@link JsonNode} containing the original
 * Yahoo Finance response structure, allowing full flexibility
 * for consumers.
 */
public class OpenFinanceData {

    private final YahooRequestCoordinator requestCoordinator;

    // -------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------

    /**
     * Creates a new instance of the OpenFinanceData library.
     * <p>
     * Internally, this initializes all required components
     * responsible for session handling, cookies, and
     * authentication with Yahoo Finance.
     */
    public OpenFinanceData() {
        this.requestCoordinator = new YahooRequestCoordinator();
    }

    // =================================================
    // QUOTE SUMMARY — PREDEFINED MODULE METHODS
    // =================================================

    /**
     * Returns the main financial fundamentals of a company.
     * <p>
     * This method queries the default modules of the
     * <b>quoteSummary</b> endpoint:
     * <ul>
     *   <li>summaryDetail</li>
     *   <li>defaultKeyStatistics</li>
     *   <li>financialData</li>
     * </ul>
     *
     * <p>
     * This is the recommended method for obtaining an
     * overall financial overview of a company.
     *
     * @param symbol the asset ticker symbol (e.g. AAPL, MSFT, PETR4.SA)
     * @return {@link JsonNode} containing the company fundamentals
     */
    public JsonNode getFundamentals(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of()
        );
    }

    /**
     * Returns profile information about a company.
     * <p>
     * Queries the following module:
     * <ul>
     *   <li>summaryProfile</li>
     * </ul>
     *
     * <p>
     * Includes data such as industry, sector, business description,
     * country, and company website.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing company profile information
     */
    public JsonNode getProfile(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of("modules", "summaryProfile")
        );
    }

    /**
     * Returns earnings-related information for a company.
     * <p>
     * Queries the following modules:
     * <ul>
     *   <li>earnings</li>
     *   <li>earningsHistory</li>
     *   <li>earningsTrend</li>
     * </ul>
     *
     * <p>
     * Includes historical earnings, quarterly results,
     * and future earnings estimates.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing earnings data
     */
    public JsonNode getEarnings(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of(
                        "modules",
                        "earnings,earningsHistory,earningsTrend"
                )
        );
    }

    /**
     * Returns historical financial statements for a company.
     * <p>
     * Queries the following modules:
     * <ul>
     *   <li>incomeStatementHistory</li>
     *   <li>balanceSheetHistory</li>
     *   <li>cashflowStatementHistory</li>
     * </ul>
     *
     * <p>
     * Contains annual and quarterly data for income statements,
     * balance sheets, and cash flow statements.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing financial statements
     */
    public JsonNode getFinancialStatements(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of(
                        "modules",
                        "incomeStatementHistory,balanceSheetHistory,cashflowStatementHistory"
                )
        );
    }

    /**
     * Returns analyst recommendations and rating changes.
     * <p>
     * Queries the following modules:
     * <ul>
     *   <li>recommendationTrend</li>
     *   <li>upgradeDowngradeHistory</li>
     * </ul>
     *
     * <p>
     * Includes analyst consensus, rating trends,
     * and upgrade/downgrade history.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing analyst recommendations
     */
    public JsonNode getAnalystRecommendations(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of(
                        "modules",
                        "recommendationTrend,upgradeDowngradeHistory"
                )
        );
    }

    /**
     * Returns upcoming corporate events for a company.
     * <p>
     * Queries the following module:
     * <ul>
     *   <li>calendarEvents</li>
     * </ul>
     *
     * <p>
     * Includes earnings dates, dividend dates,
     * and other relevant corporate events.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing corporate calendar events
     */
    public JsonNode getCalendarEvents(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of("modules", "calendarEvents")
        );
    }

    /**
     * Returns ownership and insider information for a company.
     * <p>
     * Queries the following modules:
     * <ul>
     *   <li>institutionOwnership</li>
     *   <li>fundOwnership</li>
     *   <li>insiderHolders</li>
     *   <li>insiderTransactions</li>
     * </ul>
     *
     * <p>
     * Includes institutional ownership, fund holdings,
     * and insider trading activity.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing ownership information
     */
    public JsonNode getOwnership(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                Map.of(
                        "modules",
                        "institutionOwnership,fundOwnership,insiderHolders,insiderTransactions"
                )
        );
    }

    // =================================================
    // QUOTE SUMMARY — GENERIC (ADVANCED) METHOD
    // =================================================

    /**
     * Advanced method to query any combination of
     * <b>quoteSummary</b> modules.
     *
     * <p>
     * This method is intended for advanced users who
     * want full control over which modules are returned
     * by Yahoo Finance.
     *
     * <p>
     * Example:
     * <pre>
     * Map.of("modules", "summaryDetail,financialData,earnings")
     * </pre>
     *
     * @param symbol the asset ticker symbol
     * @param params quoteSummary query parameters
     * @return {@link JsonNode} containing the raw Yahoo response
     */
    public JsonNode getQuoteSummary(
            String symbol,
            Map<String, String> params
    ) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE_SUMMARY,
                params
        );
    }

    // =================================================
    // QUOTE (PRICE)
    // =================================================

    /**
     * Returns the current quote for an asset.
     * <p>
     * Includes information such as current price,
     * price change, volume, and exchange.
     *
     * @param symbol the asset ticker symbol
     * @return {@link JsonNode} containing quote data
     */
    public JsonNode getQuote(String symbol) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.QUOTE,
                Map.of()
        );
    }

    // =================================================
    // HISTORY (CHART)
    // =================================================

    /**
     * Returns historical price data for an asset.
     * <p>
     * The {@code range} and {@code interval} parameters
     * control the time span and data granularity.
     *
     * <p>
     * Example values:
     * <ul>
     *   <li>range: 1mo, 6mo, 1y, 5y</li>
     *   <li>interval: 1d, 1wk, 1mo</li>
     * </ul>
     *
     * @param symbol the asset ticker symbol
     * @param range time range of the historical data
     * @param interval data interval
     * @return {@link JsonNode} containing historical prices
     */
    public JsonNode getHistory(
            String symbol,
            String range,
            String interval
    ) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.HISTORY,
                Map.of(
                        "range", range,
                        "interval", interval
                )
        );
    }

    /**
     * Returns historical price data including corporate events.
     * <p>
     * In addition to price data, this method allows
     * inclusion of:
     * <ul>
     *   <li>Dividends</li>
     *   <li>Stock splits</li>
     * </ul>
     *
     * <p>
     * Example values for {@code events}:
     * <ul>
     *   <li>div</li>
     *   <li>splits</li>
     *   <li>div,splits</li>
     * </ul>
     *
     * @param symbol the asset ticker symbol
     * @param range time range of the historical data
     * @param interval data interval
     * @param events events to include
     * @return {@link JsonNode} containing historical data with events
     */
    public JsonNode getHistoryWithEvents(
            String symbol,
            String range,
            String interval,
            String events
    ) {
        return requestCoordinator.requestCoordinator(
                symbol,
                YahooApiType.HISTORY,
                Map.of(
                        "range", range,
                        "interval", interval,
                        "events", events
                )
        );
    }

    // =================================================
    // SEARCH
    // =================================================

    /**
     * Searches for assets on Yahoo Finance.
     * <p>
     * Can be used to discover ticker symbols
     * based on company names or keywords.
     *
     * @param query search term (e.g. "Apple", "Tesla")
     * @return {@link JsonNode} containing search results
     */
    public JsonNode search(String query) {
        return requestCoordinator.requestCoordinator(
                null,
                YahooApiType.SEARCH,
                Map.of("query", query)
        );
    }
}
