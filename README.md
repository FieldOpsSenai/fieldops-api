# FieldOps API

API REST da plataforma **FieldOps — Plataforma de Inspeção em Campo**.

## Sobre o projeto

O `fieldops-api` é o serviço central da plataforma FieldOps. A API fornece os recursos necessários para comunicação entre o aplicativo Mobile, o painel Admin Web e o banco de dados da plataforma.

A API concentra as regras de negócio e o acesso aos dados, garantindo que os clientes da aplicação não tenham acesso direto ao banco de dados.

##  Responsabilidades

O `fieldops-api` é responsável por:

* autenticação e autorização;
* regras de negócio;
* persistência de dados;
* gerenciamento das inspeções;
* sincronização com o aplicativo Mobile;
* comunicação com o Admin Web;
* auditoria das operações.

## Arquitetura

A API funciona como camada central de comunicação entre os clientes da plataforma e o banco de dados.

```text
┌─────────────────────┐
│   fieldops-mobile   │
│    Expo + TypeScript│
└──────────┬──────────┘
           │
           │ REST API
           ▼
┌─────────────────────┐
│    fieldops-api     │
│    Spring Boot      │
└──────────┬──────────┘
           │
           │
           ▼
┌─────────────────────┐
│     PostgreSQL      │
└─────────────────────┘
           ▲
           │
           │ REST API
┌──────────┴──────────┐
│    fieldops-admin   │
│   Next.js + Tailwind│
└─────────────────────┘
```

Os clientes **Mobile** e **Admin** não acessam diretamente o banco de dados.

Toda comunicação com os dados e as regras de negócio deve ocorrer através da API.

## 🛠️ Tecnologias

* Java
* Spring Boot
* Maven
* PostgreSQL

> As versões das tecnologias serão definidas durante a configuração do projeto.

##  Estrutura do projeto

A estrutura interna será definida conforme a implementação do projeto Spring Boot.

A organização deverá seguir as convenções estabelecidas pelo projeto FieldOps.

## Configuração do ambiente

As configurações específicas do ambiente devem ser mantidas fora do código-fonte.

Informações sensíveis, como senhas, tokens e credenciais, **não devem ser versionadas no Git**.

As variáveis necessárias para execução do projeto deverão ser documentadas através de um arquivo `.env.example` ou mecanismo equivalente.

## Execução

As instruções de instalação, configuração e execução serão adicionadas após a criação da estrutura inicial do projeto Spring Boot.

##  Testes

Os testes automatizados devem ser executados antes da abertura de um Pull Request.

As instruções específicas para execução dos testes serão documentadas conforme a implementação do projeto.

## Desenvolvimento

O desenvolvimento deve seguir as convenções definidas pelo projeto FieldOps.

### Branches

As branches devem seguir o padrão:

```text
feature/PBI-XXX-descricao
fix/PBI-XXX-descricao
hotfix/PBI-XXX-descricao
```

Exemplo:

```text
feature/PBI-007-autenticacao
```

### Commits

Os commits devem seguir o padrão:

```text
tipo(PBI-ID): descrição
```

Exemplo:

```text
feat(PBI-007): adiciona autenticação
```

### Pull Requests

Toda alteração destinada à branch `main` deve ser realizada através de um Pull Request.

As regras completas de contribuição e revisão estão documentadas em:

`docs/CONTRIBUTING.md`

## Documentação

A documentação específica do repositório está disponível no diretório `docs/`.

Documentações relacionadas ao desenvolvimento e às convenções gerais do projeto devem seguir os padrões definidos pelo FieldOps.

## Status

Em desenvolvimento.
