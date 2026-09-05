# Contributing — FieldOps API

Este documento define as convenções para desenvolvimento e contribuição no repositório `fieldops-api`.

## Branch principal

A branch principal do projeto é:

```text
main
```

A `main` representa a versão integrada do projeto e possui regras de proteção configuradas no GitHub.

Alterações não devem ser realizadas diretamente na `main`.

## Branches

Cada alteração deve ser desenvolvida em uma branch própria.

### Features

```text
feature/PBI-XXX-descricao
```

Exemplo:

```text
feature/PBI-007-autenticacao
```

### Correções

```text
fix/PBI-XXX-descricao
```

Exemplo:

```text
fix/PBI-007-validacao-senha
```

### Hotfixes

```text
hotfix/PBI-XXX-descricao
```

Exemplo:

```text
hotfix/PBI-007-correcao-login
```

A descrição deve ser curta, clara e utilizar `kebab-case`.

## Commits

Os commits devem seguir o padrão:

```text
tipo(PBI-ID): descrição
```

Exemplo:

```text
feat(PBI-007): adiciona autenticação
```

O tipo deve representar a natureza da alteração.

Exemplos:

```text
feat(PBI-007): adiciona autenticação
fix(PBI-007): corrige validação de senha
docs(PBI-001): atualiza documentação
```

A descrição deve ser objetiva e representar a alteração realizada.

## Pull Requests

Toda alteração destinada à `main` deve ser realizada através de Pull Request.

O fluxo esperado é:

```text
Branch de trabalho
       │
       ▼
Desenvolvimento
       │
       ▼
Commit
       │
       ▼
Push da branch
       │
       ▼
Pull Request → main
       │
       ▼
Code Review
       │
       ▼
Aprovação
       │
       ▼
Merge
```

### Regras da `main`

O repositório possui proteção configurada para a `main`.

Entre as regras aplicadas estão:

* Pull Request obrigatório;
* pelo menos 1 aprovação;
* nova aprovação quando novos commits invalidarem a revisão anterior;
* aprovação do push mais recente;
* resolução das conversas antes do merge;
* bloqueio de force push;
* bloqueio da exclusão da branch;
* merge utilizando Squash.

## Fluxo de desenvolvimento

1. Atualizar a referência local da `main`.
2. Criar uma branch seguindo o padrão definido.
3. Desenvolver a alteração.
4. Criar commits seguindo a convenção estabelecida.
5. Fazer push da branch para o repositório remoto.
6. Abrir um Pull Request direcionado para `main`.
7. Corrigir os pontos identificados durante a revisão.
8. Garantir que as conversas estejam resolvidas.
9. Obter a aprovação necessária.
10. Realizar o merge utilizando Squash.

## Boas práticas

* Não realizar commits diretamente na `main`.
* Não utilizar `force push` na `main`.
* Não versionar senhas, tokens, credenciais ou outros dados sensíveis.
* Manter os commits relacionados à alteração realizada.
* Utilizar mensagens de commit claras e objetivas.
* Associar branches e commits ao respectivo PBI quando aplicável.
* Manter a documentação atualizada quando o processo ou comportamento documentado for alterado.

## Relação com os PBIs

Sempre que a alteração estiver relacionada a um PBI, seu identificador deve ser utilizado na branch e nos commits.

Exemplo:

```text
PBI-007
```

Branch:

```text
feature/PBI-007-autenticacao
```

Commit:

```text
feat(PBI-007): adiciona autenticação
```
