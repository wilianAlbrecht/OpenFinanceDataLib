🇧🇷 [Português](README_PT-BR.md) - 🇺🇸 [English](README.md) - 🇨🇳 [中文](README_ZH.md)


# OpenFinanceDataLib

Biblioteca Java **técnica e reutilizável** para acesso **RAW e estável** a dados do **Yahoo Finance**, projetada para ser integrada **diretamente dentro de qualquer projeto**, sem dependência de APIs externas.

---

## Documentação Completa

A documentação detalhada de uso da biblioteca está disponível nos idiomas abaixo:

- 🇧🇷 **Português** — `documentation/documentation_PT-BR`
- 🇺🇸 **English** — `documentation/documentation_EN`
- 🇨🇳 **中文** — `documentation/documentation_ZH`

> A documentação cobre instalação, uso, funções disponíveis e tratamento de erros.

---

## O que é a OpenFinanceDataLib

A **OpenFinanceDataLib** é uma biblioteca focada exclusivamente em **coletar dados do Yahoo Finance** e retorná-los em formato **RAW**, sem aplicar semântica, organização ou regras de negócio.

Ela funciona simulando o comportamento de um navegador real, lidando internamente com cookies, headers e autenticação técnica, mas **expondo ao consumidor apenas os dados brutos**, de forma previsível e estável.

A biblioteca foi pensada para desenvolvedores que precisam de **controle total sobre os dados**, desejam construir suas próprias camadas financeiras e preferem **integrar a coleta diretamente no código**, sem depender de chamadas HTTP para serviços externos.

---

## Contexto no Ecossistema OpenFinanceData

A OpenFinanceDataLib faz parte do **ecossistema OpenFinanceData**, que oferece **diferentes níveis de abstração** para acesso a dados financeiros.

Dentro do ecossistema, também existe o **OpenFinanceData Web Service**, que possui o mesmo objetivo geral — coletar dados do Yahoo Finance — porém com **uma abordagem diferente**.

Essas duas soluções **não competem entre si**: elas existem para **cenários distintos**.

---

## OpenFinanceDataLib × OpenFinanceData Web Service

Embora ambas coletem dados do Yahoo Finance, a diferença está **na forma de acesso e no nível de abstração**:

- **OpenFinanceDataLib**
  - Integração direta no projeto
  - Retorno de dados **RAW**
  - Nenhuma organização ou semântica aplicada
  - Máxima flexibilidade
  - Ideal como **fundação técnica**

- **OpenFinanceData Web Service**
  - Consumo via API HTTP
  - Dados organizados e estruturados
  - Maior abstração
  - Ideal para consumo direto por aplicações

A escolha depende do **nível de controle** e do **tipo de integração** que o projeto exige.

---

## Quando Usar a OpenFinanceDataLib

A OpenFinanceDataLib é a escolha ideal quando você:

- Precisa acessar **dados brutos** diretamente
- Quer definir **sua própria semântica e regras**
- Está construindo uma API, serviço ou pipeline financeiro
- Deseja evitar dependência de serviços externos
- Precisa integrar a coleta de dados **diretamente no backend**

A biblioteca **não impõe decisões**, não cria modelos de negócio e não interpreta os dados — essa responsabilidade é totalmente do consumidor.

---

## Filosofia

A OpenFinanceDataLib segue princípios claros:

- Separação total entre **acesso técnico** e **regra de negócio**
- Retorno sempre em formato **RAW**
- Nenhuma abstração financeira imposta
- Integração simples e previsível

---

## Nota Final

A OpenFinanceDataLib **não é uma API financeira pronta**, mas uma **base técnica sólida** para quem deseja construir soluções financeiras próprias, com total controle sobre dados, interpretação e arquitetura.
