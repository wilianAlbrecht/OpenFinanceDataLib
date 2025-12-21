# OpenFinanceDataLib — Usage Guide

A **technical and reusable Java library** for stable access to **RAW Yahoo Finance data**.

This documentation focuses **exclusively on how to use the library**: how to install it, how to instantiate it, which functions are available, and how to use them correctly.

---

## Installation

### Maven

```xml
<dependency>
  <groupId>com.openfinancedata</groupId>
  <artifactId>open-finance-data-lib</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Gradle

```gradle
dependencies {
    implementation "com.openfinancedata:open-finance-data-lib:1.0.1"
}
```

> No initial configuration is required.

---

## Basic Usage

### Instantiation

```java
OpenFinanceData openFinanceData = new OpenFinanceData();
```

- Empty constructor
- Session, cookies, and crumb are initialized automatically
- Crumb cache is shared across the JVM

---

## Important Concept

**All functions return RAW `JsonNode` data**.

The library **does not interpret**, **does not apply semantics**, and **does not convert** the data.

The consumer is responsible for:
- Reading fields
- Validating presence
- Converting types
- Applying business rules

---

## Available Functions

### Quote Summary (Fundamentals)

Retrieves data from Yahoo Finance’s `quoteSummary` endpoint.

```java
JsonNode data = openFinanceData.getFundamentals("AAPL");
```

Included modules:
- `summaryDetail`
- `defaultKeyStatistics`
- `financialData`

Response:
```json
{
  "quoteSummary": {
    "result": [ { ... } ],
    "error": null
  }
}
```

---

### Quote (Current Price)

```java
JsonNode quote = openFinanceData.getQuote("AAPL");
```

---

### Multiple Quotes

```java
JsonNode quotes = openFinanceData.getQuotes(List.of("AAPL", "MSFT", "GOOGL"));
```

---

### Price History

```java
JsonNode history = openFinanceData.getHistory(
    "AAPL",
    "1y",
    "1d"
);
```

---

### Earnings

```java
JsonNode earnings = openFinanceData.getEarnings("AAPL");
```

---

### Profile (Company)

```java
JsonNode profile = openFinanceData.getProfile("AAPL");
```

---

### Financials (Income Statement, Balance Sheet, Cash Flow)

```java
JsonNode financials = openFinanceData.getFinancials("AAPL");
```

---

### Search (Asset Lookup)

```java
JsonNode search = openFinanceData.search("Apple");
```

---

## Public API Mapping

| Function | Description |
|---------|-------------|
| `getFundamentals(String symbol)` | Quote Summary (fundamentals) |
| `getQuote(String symbol)` | Current quote |
| `getQuotes(List<String>)` | Multiple quotes |
| `getHistory(String, String, String)` | Price history |
| `getEarnings(String)` | Earnings |
| `getProfile(String)` | Company profile |
| `getFinancials(String)` | Financial data |
| `search(String query)` | Asset search |

---

## Error Handling

The OpenFinanceDataLib exposes **explicit technical exceptions**, allowing consumers to **clearly identify the type of failure** and decide how to react.

---

### `YahooAuthException`
Technical authentication issue with Yahoo Finance.

### `YahooRateLimitException`
Request rate exceeded.

### `YahooUnavailableException`
Yahoo Finance service unavailable.

### `YahooSchemaException`
Unexpected or changed JSON schema.

---

## Tests

Integration tests validate:
- Preventive crumb flow
- Controlled retry behavior
- Public API stability

---

## Best Practices

- Cache results when calling repeatedly
- Never assume fields always exist
- Handle `null` and `error`
- Use defensive parsing

---

## Final Note

This library **is not a financial API**, but a **stable technical access layer** to Yahoo Finance.
