🇧🇷 [Português](README_PT-BR.md) - 🇺🇸 [English](README.md) - 🇨🇳 [中文](README_ZH.md)

# OpenFinanceDataLib

A **technical and reusable Java library** for **stable RAW access** to **Yahoo Finance data**, designed to be integrated **directly into any project**, without relying on external APIs.

---

## Complete Documentation

Detailed usage documentation is available in the following languages:

- 🇧🇷 [Português](documentation/documentation_PT-BR.me)
- 🇺🇸 [English](documentation/documentation_EN.me)
- 🇨🇳 [中文](documentation/documentation_ZH.me)
- 
> The documentation covers installation, usage, available functions, and error handling.

---

## What is OpenFinanceDataLib

**OpenFinanceDataLib** is a **low-level Java library** focused exclusively on **collecting data from Yahoo Finance** and returning it in **RAW format**, without applying semantics, organization, or business rules.

It works by simulating the behavior of a real browser, internally handling cookies, headers, and technical authentication, while **exposing only raw data** to the consumer in a predictable and stable way.

The library is designed for developers who need **full control over data**, want to build their own financial layers, and prefer to **integrate data collection directly into their code**, without depending on HTTP calls to external services.

---

## Context within the OpenFinanceData Ecosystem

OpenFinanceDataLib is part of the **OpenFinanceData ecosystem**, which provides **different abstraction levels** for accessing financial data.

Within the ecosystem, there is also the **OpenFinanceData Web Service**, which shares the same overall goal — collecting data from Yahoo Finance — but follows **a different approach**.

These two solutions **do not compete** with each other; they exist for **different use cases**.

---

## OpenFinanceDataLib × OpenFinanceData Web Service

Although both collect data from Yahoo Finance, the difference lies in **how the data is accessed and abstracted**:

- **OpenFinanceDataLib**
  - Direct integration into the project
  - **RAW** data output
  - No organization or semantic processing
  - Maximum flexibility
  - Ideal as a **technical foundation**

- **OpenFinanceData Web Service**
  - Consumption via HTTP API
  - Organized and structured data
  - Higher abstraction level
  - Ideal for direct consumption by applications

The choice depends on the **level of control** and **type of integration** required by your project.

---

## When to Use OpenFinanceDataLib

OpenFinanceDataLib is the right choice when you:

- Need direct access to **raw data**
- Want to define **your own semantics and rules**
- Are building a financial API, service, or data pipeline
- Want to avoid dependency on external services
- Need to integrate data collection **directly into backend code**

The library **does not impose decisions**, does not create business models, and does not interpret data — this responsibility lies entirely with the consumer.

---

## Philosophy

OpenFinanceDataLib follows clear principles:

- Full separation between **technical access** and **business logic**
- Always returns **RAW data**
- No financial abstractions imposed
- Simple and predictable integration

---

## Final Note

OpenFinanceDataLib **is not a ready-made financial API**, but a **solid technical foundation** for building custom financial solutions with full control over data, interpretation, and architecture.
