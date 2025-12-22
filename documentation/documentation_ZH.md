# OpenFinanceDataLib — 使用指南

一个**技术性、可复用的 Java 库**，用于稳定访问 **Yahoo Finance 的原始（RAW）数据**。

本文档**仅关注库的使用方式**：如何安装、如何实例化、可用方法以及正确的使用方式。

---

## 安装

### Maven

```xml
<dependency>
    <groupId>io.github.wilianalbrecht</groupId>
    <artifactId>openfinancedata-lib</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle

```gradle
dependencies {
    implementation "com.openfinancedata:open-finance-data-lib:1.0.1"
}
```

> 无需任何初始配置。

---

## 基本使用

### 实例化

```java
OpenFinanceData openFinanceData = new OpenFinanceData();
```

- 空构造函数
- 会话、Cookies 和 Crumb 自动初始化
- Crumb 缓存在 JVM 范围内共享

---

## 重要概念

**所有方法均返回原始 `JsonNode` 数据**。

该库**不做语义解析**、**不进行数据转换**。

使用者需要负责：
- 读取字段
- 校验字段存在性
- 类型转换
- 业务规则处理

---

## 可用功能

### Quote Summary（基本面）

```java
JsonNode data = openFinanceData.getFundamentals("AAPL");
```

---

### 当前价格（Quote）

```java
JsonNode quote = openFinanceData.getQuote("AAPL");
```

---

### 多股票查询

```java
JsonNode quotes = openFinanceData.getQuotes(List.of("AAPL", "MSFT", "GOOGL"));
```

---

### 历史价格

```java
JsonNode history = openFinanceData.getHistory(
    "AAPL",
    "1y",
    "1d"
);
```

---

### Earnings（财报）

```java
JsonNode earnings = openFinanceData.getEarnings("AAPL");
```

---

### 公司信息（Profile）

```java
JsonNode profile = openFinanceData.getProfile("AAPL");
```

---

### 财务数据（利润表 / 资产负债表 / 现金流）

```java
JsonNode financials = openFinanceData.getFinancials("AAPL");
```

---

### 搜索资产

```java
JsonNode search = openFinanceData.search("Apple");
```

---

## API 映射

| 方法 | 描述 |
|------|------|
| `getFundamentals(String)` | 基本面数据 |
| `getQuote(String)` | 当前价格 |
| `getQuotes(List<String>)` | 多股票 |
| `getHistory(String, String, String)` | 历史价格 |
| `getEarnings(String)` | 财报 |
| `getProfile(String)` | 公司信息 |
| `getFinancials(String)` | 财务数据 |
| `search(String)` | 搜索资产 |

---

## 异常处理

该库提供明确的技术异常，用于区分不同类型的技术问题。

---

## 最终说明

该库**不是金融 API**，而是一个**稳定的 Yahoo Finance 技术访问层**。
