# CI e checks obrigatorios

O projeto possui um workflow de GitHub Actions em `.github/workflows/ci.yml`.
Ele e executado em Pull Requests e em pushes para a branch `main`.

## Checks executados

O workflow possui tres jobs independentes ou dependentes conforme a etapa:

1. `Maven validation`: executa `./mvnw -B -ntp validate`.
2. `Tests`: inicia PostgreSQL 15 e executa `./mvnw -B -ntp test`.
3. `Build`: executa `./mvnw -B -ntp package -DskipTests` somente depois que a validacao e os testes passam.

O job de testes usa os mesmos valores esperados atualmente pela API:

```text
Banco: fieldops_db
Usuario: postgres
Senha: 200523
Porta: 5432
```

## Configurar bloqueio de merge

Essa configuracao precisa ser feita nas regras do repositorio no GitHub:

1. Abra `Settings` > `Branches`.
2. Crie ou edite a regra da branch `main`.
3. Ative `Require a pull request before merging`.
4. Ative `Require status checks to pass before merging`.
5. Selecione estes checks como obrigatorios:
   - `Maven validation`;
   - `Tests`;
   - `Build`.
6. Ative `Require branches to be up to date before merging`, quando a equipe quiser exigir que o PR esteja atualizado com `main`.
7. Ative `Do not allow bypassing the above settings`, se administradores tambem precisarem respeitar os checks.
8. Salve a regra.

Depois disso, um Pull Request nao podera ser mesclado enquanto qualquer check obrigatorio estiver falhando ou pendente.

## Observacao

O bloqueio de merge nao e definido pelo arquivo YAML. O workflow publica os checks; a regra de protecao da `main` no GitHub e que determina se eles sao obrigatorios.