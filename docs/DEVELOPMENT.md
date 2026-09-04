# Fluxo de Desenvolvimento

Este documento define o fluxo de desenvolvimento utilizado pela equipe do FieldOps.

## 1. Seleção da PBI

Todo desenvolvimento deve estar relacionado a uma PBI registrada no GitHub Projects.

Exemplo:

```text
PBI-007 — Autenticação
```

A PBI deve estar refinada e pronta para desenvolvimento antes da criação da branch.

## 2. Criação da branch

Toda alteração deve ser desenvolvida em uma branch própria, criada a partir da `main` atualizada.

### Feature

```text
feature/PBI-XXX-descricao
```

Exemplo:

```text
feature/PBI-007-autenticacao
```

### Correção

```text
fix/PBI-XXX-descricao
```

Exemplo:

```text
fix/PBI-014-sincronizacao-offline
```

### Hotfix

```text
hotfix/PBI-XXX-descricao
```

Exemplo:

```text
hotfix/PBI-021-falha-login
```

## 3. Atualização da branch

Antes de iniciar o desenvolvimento, a branch `main` deve estar atualizada.

```bash
git checkout main
git pull origin main
git checkout -b feature/PBI-XXX-descricao
```

## 4. Desenvolvimento

O desenvolvimento deve ser realizado exclusivamente na branch criada para a PBI.

As alterações devem ser pequenas e relacionadas ao objetivo da PBI.

## 5. Commits

Os commits devem seguir a convenção definida pelo projeto:

```text
tipo(PBI-ID): descrição
```

Exemplo:

```text
feat(PBI-007): adiciona autenticação
```

Consulte a documentação de convenções para os tipos de commit permitidos.

## 6. Push

Após realizar os commits, a branch deve ser enviada para o repositório remoto:

```bash
git push -u origin feature/PBI-XXX-descricao
```

## 7. Pull Request

Quando o desenvolvimento estiver concluído, deve ser aberto um Pull Request para a branch `main`.

Exemplo:

```text
PBI-007: implementa autenticação
```

O Pull Request deve permitir a revisão do código e a execução das verificações automatizadas.

## 8. Code Review

O Pull Request deve ser revisado por pelo menos um membro da equipe antes do merge.

A revisão deve verificar, quando aplicável:

* atendimento aos critérios de aceitação da PBI;
* qualidade e organização do código;
* testes;
* possíveis impactos em outros componentes;
* segurança;
* documentação.

## 9. Integração

Após a aprovação do Pull Request e a conclusão das verificações obrigatórias, a alteração poderá ser integrada à `main`.

O método de merge adotado pelo projeto será definido nas regras de Pull Request.

## 10. Exclusão da branch

Após o merge, a branch utilizada para a PBI deve ser excluída do repositório remoto.

## 11. Rastreabilidade

Toda alteração deve permitir rastrear sua origem:

```text
PBI
 ↓
Branch
 ↓
Commit
 ↓
Pull Request
 ↓
main
```

O ID da PBI deve estar presente no nome da branch e nos commits relacionados.

## 12. Regra geral

Não deve ser realizado desenvolvimento diretamente na `main`.

Toda alteração destinada à `main` deve passar pelo fluxo:

```text
PBI
 ↓
Branch
 ↓
Desenvolvimento
 ↓
Commit
 ↓
Push
 ↓
Pull Request
 ↓
Code Review
 ↓
Verificações
 ↓
Merge
 ↓
main
```
