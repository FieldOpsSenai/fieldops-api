# FieldOps API

API REST da plataforma FieldOps — Plataforma de Inspeção em Campo.

## Responsabilidade

O `fieldops-api` é responsável por:

- autenticação e autorização;
- regras de negócio;
- persistência de dados;
- gerenciamento das inspeções;
- sincronização com o aplicativo Mobile;
- comunicação com o Admin Web;
- auditoria das operações.

## Arquitetura

O `fieldops-api` funciona como camada central de comunicação entre os clientes da plataforma e o banco de dados.

```text
fieldops-mobile ──┐
                  ├──> fieldops-api ──> PostgreSQL
fieldops-admin  ──┘
