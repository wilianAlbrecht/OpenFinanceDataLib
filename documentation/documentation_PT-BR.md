# OpenFinanceDataLib — Guia de Uso

Biblioteca Java **técnica e reutilizável** para acesso estável aos **dados RAW do Yahoo Finance**.

Esta documentação foca **exclusivamente no uso da lib**: como instalar, como instanciar, quais funções estão disponíveis e como utilizá‑las corretamente.

---

## Instalação

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

> A lib não exige nenhuma configuração inicial.

---

## Uso Básico

### Instanciação

```java
OpenFinanceData openFinanceData = new OpenFinanceData();
```

- Construtor vazio
- Sessão, cookies e crumb são inicializados automaticamente
- Cache de crumb é compartilhado por toda a JVM

---

## Conceito Importante

**Todas as funções retornam `JsonNode` RAW**.

A lib **não interpreta**, **não valida semântica**, **não converte** os dados.

O consumidor é responsável por:
- Ler campos
- Validar presença
- Converter tipos
- Aplicar regras de negócio

---

## Funções Disponíveis

### Quote Summary (Fundamentals)

Obtém os dados do endpoint `quoteSummary` do Yahoo Finance.

```java
JsonNode data = openFinanceData.getFundamentals("AAPL");
```

Inclui módulos como:
- `summaryDetail`
- `defaultKeyStatistics`
- `financialData`

Retorno:
```json
{
  "quoteSummary": {
    "result": [ { ... } ],
    "error": null
  }
}
```

---

### Quote (Preço Atual)

Obtém dados de cotação atual.

```java
JsonNode quote = openFinanceData.getQuote("AAPL");
```

Retorno contém campos como:
- `regularMarketPrice`
- `regularMarketChange`
- `marketCap`

---

### Multiple Quotes

Busca múltiplos símbolos em uma única chamada.

```java
JsonNode quotes = openFinanceData.getQuotes(List.of("AAPL", "MSFT", "GOOGL"));
```

---

### Histórico de Preços

```java
JsonNode history = openFinanceData.getHistory(
    "AAPL",
    "1y",      // range
    "1d"       // interval
);
```

Parâmetros comuns:
- `range`: `1d`, `5d`, `1mo`, `6mo`, `1y`, `5y`, `max`
- `interval`: `1m`, `5m`, `15m`, `1d`, `1wk`, `1mo`

---

### Earnings

```java
JsonNode earnings = openFinanceData.getEarnings("AAPL");
```

---

### Profile (Empresa)

```java
JsonNode profile = openFinanceData.getProfile("AAPL");
```

---

### Financials (DRE, Balanço, Cash Flow)

```java
JsonNode financials = openFinanceData.getFinancials("AAPL");
```

---

### Search (Busca por Ativos)

```java
JsonNode search = openFinanceData.search("Apple");
```

---

## Mapeamento Completo da API Pública

| Função | Descrição |
|------|----------|
| `getFundamentals(String symbol)` | Quote Summary (fundamentals) |
| `getQuote(String symbol)` | Cotação atual |
| `getQuotes(List<String>)` | Cotação múltipla |
| `getHistory(String, String, String)` | Histórico de preços |
| `getEarnings(String)` | Earnings |
| `getProfile(String)` | Perfil da empresa |
| `getFinancials(String)` | Dados financeiros |
| `search(String query)` | Busca por ativos |

---

## Tratamento de Erros

A OpenFinanceDataLib expõe **exceções técnicas explícitas**, para que o consumidor consiga **diferenciar claramente o tipo de falha** e decidir como reagir.

Essas exceções **não representam erros de negócio**, apenas problemas técnicos de acesso, autenticação, schema ou disponibilidade do Yahoo Finance.

---

### `YahooAuthException`

Indica um problema de **autenticação técnica** com o Yahoo Finance.

Possíveis causas:
- Crumb inválido ou expirado
- Cookies de sessão rejeitados
- Mudança inesperada no mecanismo de autenticação do Yahoo

Comportamento da lib:
- A lib **tenta se recuperar automaticamente**:
  - Invalida o crumb em cache
  - Gera um novo crumb
  - Executa **um único retry**
- A exceção só é propagada se o retry falhar

Quando tratar:
- Geralmente não é erro do consumidor
- Pode indicar instabilidade temporária ou mudança no Yahoo

Exemplo:
```java
try {
    openFinanceData.getQuote("AAPL");
} catch (YahooAuthException e) {
    // Falha técnica de autenticação com o Yahoo
}
```

---

### `YahooRateLimitException`

Indica que o Yahoo Finance **bloqueou temporariamente** as requisições por excesso de chamadas.

Possíveis causas:
- Muitas requisições em curto período
- Uso em loop sem cache
- Uso concorrente intenso

Boas práticas:
- Implementar cache no consumidor
- Aplicar backoff ou retry externo
- Evitar polling agressivo

Exemplo:
```java
catch (YahooRateLimitException e) {
    // Reduzir taxa de chamadas ou aguardar
}
```

---

### `YahooUnavailableException`

Indica que o Yahoo Finance está **indisponível ou instável**.

Possíveis causas:
- Timeout
- Erro 5xx
- Falha de conectividade

Quando ocorre:
- O Yahoo não respondeu corretamente
- Não há erro de autenticação nem de schema

Uso recomendado:
- Retry externo com delay
- Fallback de dados
- Retornar erro temporário ao usuário final

Exemplo:
```java
catch (YahooUnavailableException e) {
    // Serviço externo indisponível
}
```

---

### `YahooSchemaException`

Indica que o **formato do JSON retornado mudou** ou não corresponde ao esperado.

Possíveis causas:
- Alteração no schema do Yahoo
- Endpoint retornou estrutura inesperada
- Campo obrigatório ausente

Importante:
- A lib **não tenta corrigir schema**
- Essa exceção é intencional para alertar o consumidor

Quando tratar:
- Revisar parsing
- Ajustar lógica do consumidor
- Atualizar versão da lib se necessário

Exemplo:
```java
catch (YahooSchemaException e) {
    // Estrutura de resposta inesperada
}
```

---


## Testes

A lib possui testes de integração que validam:
- Fluxo de crumb preventivo
- Retry controlado
- Estabilidade do contrato público

---

## Boas Práticas de Uso

- Cache o resultado se for consumir repetidamente
- Nunca assuma que campos sempre existirão
- Trate `null` e `error` no JSON
- Faça parsing defensivo

---

## Observação Final

Esta lib **não é uma API financeira**, mas um **acesso técnico estável** ao Yahoo Finance.

Se você precisa de semântica, normalização ou indicadores, isso deve existir **em outra camada** ou utilize o Web Service [OpenFinanceData](https://github.com/wilianAlbrecht/OpenFinanceData.git).

