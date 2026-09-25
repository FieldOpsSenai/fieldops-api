# CI da API

O workflow de GitHub Actions esta em `.github/workflows/ci.yml` e automatiza a
validacao, os testes e o build da API.

## Quando o workflow executa

O workflow possui dois gatilhos:

- `pull_request` para a branch `main`: executa os checks em cada Pull Request
   direcionado para `main`;
- `push` para a branch `main`: executa os checks depois que uma alteracao chega
   em `main`.

## Configuracoes gerais

### Permissoes

```yaml
permissions:
   contents: read
```

Concede ao workflow somente permissao de leitura do codigo do repositorio. Isso
reduz o acesso do job ao minimo necessario para fazer checkout e executar o
build.

### Concorrencia

```yaml
concurrency:
   group: api-ci-${{ github.workflow }}-${{ github.ref }}
   cancel-in-progress: true
```

Agrupa execucoes do mesmo workflow por referencia. Se um novo commit for
enviado antes da conclusao de uma execucao anterior, a execucao antiga e
cancelada para evitar trabalho desnecessario.

## Jobs executados

### `Maven validation`

Esse job verifica se o projeto Maven esta corretamente configurado, sem
compilar ou executar os testes:

```bash
./mvnw -B -ntp validate
```

- `./mvnw`: usa a versao do Maven definida pelo projeto;
- `-B`: executa o Maven em modo nao interativo;
- `-ntp`: desativa a exibicao do progresso de download;
- `validate`: verifica a estrutura e a configuracao do projeto.

### `Tests`

Esse job executa os testes automatizados da API. Antes dos testes, o GitHub
Actions inicia um servico PostgreSQL 15 para que os testes de integracao possam
carregar o contexto do Spring e acessar o banco.

```bash
./mvnw -B -ntp test
```

O servico PostgreSQL possui uma verificacao de saude com `pg_isready`. O job so
prossegue quando o banco estiver pronto para receber conexoes.

O job usa os mesmos valores esperados atualmente pela API:

```text
Banco: fieldops_db
Usuario: postgres
Senha: 200523
Porta: 5432
```

### `Build`

Esse job gera o artefato distribuivel da API:

```bash
./mvnw -B -ntp package -DskipTests
```

- `package`: compila o codigo e gera o arquivo JAR;
- `-DskipTests`: nao executa os testes novamente, pois eles ja foram executados
   no job `Tests`;
- `needs`: faz o build depender dos jobs `Maven validation` e `Tests`.

Assim, o build so e considerado concluido depois que a validacao e os testes
terminarem com sucesso.

## Fluxo resumido

```text
Pull Request ou push para main
                     |
                     +--> Maven validation
                     |
                     +--> Tests + PostgreSQL
                                     |
                                     +--> Build
```