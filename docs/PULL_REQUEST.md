# Fluxo de Pull Request

Este documento define o fluxo de Pull Request (PR) utilizado pela equipe do FieldOps para revisar, validar e integrar alterações na branch `main`.

## 1. Objetivo

Todo código destinado à `main` deve passar por um Pull Request.

O fluxo tem como objetivos:

* garantir revisão das alterações;
* verificar o atendimento aos critérios da PBI;
* executar validações automatizadas;
* reduzir riscos de regressão;
* manter a rastreabilidade entre PBI, branch, commits e código integrado.

## 2. Criação do Pull Request

Após concluir o desenvolvimento da PBI, o desenvolvedor deve enviar sua branch para o repositório remoto e abrir um Pull Request direcionado à `main`.

Exemplo de branch:

```text
feature/PBI-007-autenticacao
```

Título do Pull Request:

```text
PBI-007: implementa autenticação
```

O Pull Request deve estar relacionado à PBI correspondente no GitHub Projects.

## 3. Descrição do Pull Request

A descrição do PR deve apresentar, de forma objetiva:

* PBI relacionada;
* resumo das alterações;
* critérios de aceitação atendidos;
* testes realizados;
* possíveis impactos ou observações.

## 4. Code Review

Todo Pull Request deve ser revisado por pelo menos um membro da equipe antes do merge.

O revisor deve verificar, quando aplicável:

* atendimento aos critérios de aceitação;
* qualidade e organização do código;
* aderência às convenções do projeto;
* testes;
* segurança;
* tratamento de erros;
* possíveis impactos em outros componentes;
* documentação necessária.

## 5. Correções solicitadas

Caso sejam identificados problemas durante a revisão, o desenvolvedor deve realizar as correções na mesma branch do Pull Request.

Novos commits podem ser adicionados à branch até que as pendências sejam resolvidas.

Após as correções, o revisor deve reavaliar o Pull Request.

## 6. Verificações automatizadas

O Pull Request deverá passar pelas verificações automatizadas configuradas no repositório.

Quando aplicável, as verificações devem incluir:

* compilação;
* testes automatizados;
* análise estática;
* validações de qualidade;
* outras verificações definidas pelo projeto.

Um Pull Request não deve ser integrado caso uma verificação obrigatória esteja falhando.

## 7. Aprovação

O Pull Request poderá ser integrado somente após:

* aprovação de pelo menos um revisor;
* conclusão das verificações obrigatórias;
* resolução dos comentários relevantes;
* atendimento aos critérios de aceitação da PBI.

## 8. Merge

O merge deve ser realizado pelo GitHub após a aprovação do Pull Request e a conclusão das verificações obrigatórias.

A estratégia de merge adotada pelo projeto será definida nas configurações de proteção da branch `main`.

Não deve ser realizado merge diretamente pelo desenvolvedor ignorando o Pull Request.

## 9. Branch após o merge

Após a integração do Pull Request, a branch utilizada para desenvolvimento deve ser excluída do repositório remoto.

Exemplo:

```text
feature/PBI-007-autenticacao
```

A branch `main` permanece como referência principal do código integrado.

## 10. Pull Request rejeitado ou fechado

Caso um Pull Request seja fechado sem merge, a branch poderá ser:

* corrigida e utilizada novamente, quando fizer sentido;
* ou excluída caso não seja mais necessária.

A PBI deve permanecer atualizada no GitHub Projects conforme a situação.

## 11. Rastreabilidade

Todo Pull Request deve permitir identificar a PBI que originou a alteração.

Fluxo de rastreabilidade:

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

Exemplo:

```text
PBI-007
 ↓
feature/PBI-007-autenticacao
 ↓
feat(PBI-007): adiciona autenticação
 ↓
PBI-007: implementa autenticação
 ↓
main
```

## 12. Regra geral

A `main` deve permanecer protegida e estável.

O fluxo padrão para qualquer alteração é:

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
Aprovação
 ↓
Merge
 ↓
main
```

Não é permitido desenvolver diretamente na `main` nem realizar alterações diretamente nela ignorando o fluxo de Pull Request.

