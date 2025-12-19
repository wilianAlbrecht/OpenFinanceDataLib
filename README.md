# OpenFinanceDataLib

OpenFinanceDataLib is a lightweight Java library that provides access to public
financial data from Yahoo Finance.

The library focuses on **raw data access**, exposing Yahoo responses as-is,
without imposing DTOs or business logic, making it ideal for APIs, data
pipelines, and financial analysis projects.

---

## ✨ Features

- Company fundamentals (quoteSummary)
- Real-time and delayed quotes
- Historical price data (chart)
- Earnings, financial statements and analyst data
- Asset search
- Automatic session, cookie and crumb handling
- Built-in response validation and error classification
- No Spring dependency
- Java 21 compatible

---

## 🚀 Getting Started

### Requirements

- Java 21+
- Maven

### Installation (local)

```bash
mvn clean install


## Basic Usage

OpenFinanceData api = new OpenFinanceData();

// Fundamentals
JsonNode fundamentals = api.getFundamentals("AAPL");

// Quote
JsonNode quote = api.getQuote("AAPL");

// History
JsonNode history = api.getHistory("AAPL", "1y", "1d");

// Search
JsonNode search = api.search("Apple");
