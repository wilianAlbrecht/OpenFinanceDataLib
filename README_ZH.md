🇧🇷 [Português](README_PT-BR.md) - 🇺🇸 [English](README.md) - 🇨🇳 [中文](README_ZH.md)

# OpenFinanceDataLib

一个**技术型、可复用的 Java 库**，用于**稳定地获取 Yahoo Finance 的原始（RAW）数据**，可**直接集成到任何项目中**，无需依赖外部 API。

---

## 完整文档

该库的详细使用文档提供以下语言版本：

- 🇧🇷 [Português](/documentation/documentation_PT-BR.md)
- 🇺🇸 [English](/documentation/documentation_EN.md)
- 🇨🇳 [中文](/documentation/documentation_ZH.md)

> 文档涵盖安装方式、使用方法、可用功能以及错误处理。

---

## 什么是 OpenFinanceDataLib

**OpenFinanceDataLib** 是一个**低层级的 Java 库**，专注于**从 Yahoo Finance 获取数据**，并以**RAW 原始格式**返回，不进行语义处理、数据组织或业务规则应用。

该库通过**模拟真实浏览器行为**工作，在内部处理 cookies、headers 以及技术认证，同时**仅向使用者暴露原始数据**，确保行为可预测且稳定。

它适用于需要**完全掌控数据**、希望构建自定义金融层，并且倾向于**在代码中直接集成数据采集**、而非依赖外部 HTTP 服务的开发者。

---

## OpenFinanceData 生态中的定位

OpenFinanceDataLib 是 **OpenFinanceData 生态系统**的一部分，该生态提供了**不同抽象层级**的金融数据访问方式。

在该生态中，还存在 **OpenFinanceData Web Service**，其目标同样是获取 Yahoo Finance 数据，但采用了**不同的实现方式**。

这两种方案**并非竞争关系**，而是针对**不同使用场景**而设计。

---

## OpenFinanceDataLib × OpenFinanceData Web Service

虽然二者都从 Yahoo Finance 获取数据，但主要区别在于**访问方式和抽象层级**：

- **OpenFinanceDataLib**
  - 直接集成到项目中
  - 返回 **RAW 原始数据**
  - 不进行数据组织或语义处理
  - 最大灵活性
  - 适合作为**技术基础层**

- **OpenFinanceData Web Service**
  - 通过 HTTP API 访问
  - 数据已整理并结构化
  - 抽象层级更高
  - 适合应用直接使用

选择哪种方式，取决于项目所需的**控制程度**和**集成方式**。

---

## 何时使用 OpenFinanceDataLib

在以下情况下，OpenFinanceDataLib 是理想选择：

- 需要直接访问**原始数据**
- 希望定义**自有语义和规则**
- 正在构建金融 API、服务或数据管道
- 希望避免对外部服务的依赖
- 需要在后端代码中**直接集成数据采集**

该库**不强加任何业务决策**，不创建业务模型，也不解释数据 —— 这些责任完全由使用者承担。

---

## 设计理念

OpenFinanceDataLib 遵循以下原则：

- **技术访问**与**业务逻辑**彻底分离
- 始终返回 **RAW 原始数据**
- 不施加金融抽象
- 集成方式简单且可预测

---

## 最终说明

OpenFinanceDataLib **不是现成的金融 API**，而是一个**稳固的技术基础**，用于构建具有完全数据控制权、解释权和架构自由度的金融解决方案。
