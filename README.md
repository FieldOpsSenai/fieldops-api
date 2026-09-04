# FieldOps API

API REST da plataforma **FieldOps — Plataforma de Inspeção em Campo**.

## Responsabilidade

O `fieldops-api` é responsável pela camada central da plataforma, incluindo:

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
fieldops-mobile ──┐
                   │
                   ▼
              fieldops-api
                   │
                   ▼
               PostgreSQL
                   ▲
                   │
              fieldops-admin
```

Os clientes **Mobile** e **Admin** não acessam diretamente o banco de dados.

Toda comunicação com os dados e regras de negócio deve ocorrer através da API.

## Tecnologias

* Java
* Spring Boot
* Maven
* PostgreSQL

> As versões das tecnologias serão definidas durante a configuração do projeto.

## Estrutura do projeto

A estrutura interna será definida conforme a implementação do projeto Spring Boot.

## Configuração do ambiente

As configurações específicas do ambiente devem ser mantidas fora do código-fonte.

Informações sensíveis, como senhas e credenciais, **não devem ser versionadas no Git**.

Um arquivo `.env.example` ou mecanismo equivalente poderá ser utilizado para documentar as variáveis necessárias.

## Execução

As instruções de instalação, configuração e execução serão adicionadas após a criação da estrutura inicial do projeto.

## Testes

Os testes automatizados devem ser executados antes da abertura de um Pull Request.

As instruções específicas para execução dos testes serão documentadas conforme a implementação do projeto.

## Desenvolvimento

O desenvolvimento deve seguir as convenções definidas pelo projeto FieldOps.

### Branches

```text
feature/PBI-XXX-descricao
fix/PBI-XXX-descricao
hotfix/PBI-XXX-descricao
```

### Commits

```text
tipo(PBI-ID): descrição
```

Exemplo:

```text
feat(PBI-007): adiciona autenticação
```

### Pull Requests

Toda alteração destinada à `main` deve ser realizada através de Pull Request.

## Documentação

A documentação geral do projeto e as decisões de desenvolvimento devem ser mantidas de forma centralizada no projeto FieldOps.

## Status

Em desenvolvimento.
