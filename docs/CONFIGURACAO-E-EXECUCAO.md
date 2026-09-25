# Configuracao e execucao da API

Este documento registra a configuracao utilizada para executar a `fieldops-api` e o procedimento para iniciar o backend localmente.

## Versoes utilizadas

| Componente | Versao |
| --- | --- |
| Spring Boot | `3.2.5` |
| Java (JDK) | `21.0.12.1` |
| Hibernate ORM | `6.4.4.Final` |
| Tomcat embutido | `10.1.20` |
| Banco de dados | PostgreSQL |
| Versao do artefato | `0.0.1-SNAPSHOT` |
| Maven Wrapper | `3.3.4` |
| Maven distribuido pelo wrapper | `3.9.16` |

As versoes do Spring Boot, do Java minimo do projeto e do artefato estao declaradas em [pom.xml](../pom.xml). Hibernate e Tomcat sao dependencias gerenciadas pelo Spring Boot.

## Pre-requisitos

Instale e verifique:

- JDK 21, com `JAVA_HOME` apontando para a instalacao correta;
- Docker Desktop, caso o PostgreSQL seja executado por container;
- Git;
- acesso a internet na primeira execucao do Maven Wrapper, para baixar o Maven e as dependencias.

Verifique as versoes:

```bash
java -version
```

O resultado deve informar Java `21.0.12.1` ou outra versao Java 21 homologada para o ambiente.

## Configuracao do banco

A configuracao atualmente carregada pela API esta em [application.yml](../src/main/resources/application.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fieldops_db
    username: postgres
    password: 200523
```

O banco PostgreSQL precisa estar acessivel em `localhost:5432`, com o banco, usuario e senha correspondentes a esses valores.

### Usando o Docker Compose

O arquivo [docker-compose.yml](../docker-compose.yml) define atualmente outros valores:

```yaml
POSTGRES_DB: fieldops
POSTGRES_USER: admim
POSTGRES_PASSWORD: admim123
```

Antes de iniciar a API, alinhe o `application.yml` com o `docker-compose.yml` ou configure o PostgreSQL externo para atender aos valores usados pela API. Sem esse alinhamento, a aplicacao pode falhar com erro de banco inexistente, usuario inexistente ou falha de autenticacao.

O Compose tambem referencia o arquivo `init_fieldops.sql`. Confirme que esse arquivo existe na pasta `fieldops-api` antes de executar o Compose.

Para iniciar o PostgreSQL:

```bash
cd fieldops-api
docker compose up -d postgres
docker compose ps
```

## Executando a API

Abra um terminal na pasta `fieldops-api` e execute o Maven Wrapper.

### Windows PowerShell

```powershell
cd fieldops-api
.\mvnw.cmd spring-boot:run
```

### Linux, macOS ou Git Bash

```bash
cd fieldops-api
./mvnw spring-boot:run
```

O servidor sera iniciado na porta `8080`.

Para gerar o artefato sem iniciar a aplicacao:

```bash
./mvnw clean package
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean package
```

Para executar os testes:

```bash
./mvnw test
```

## Endpoints de verificacao

Com a API em execucao, os enderecos principais sao:

- Swagger UI: http://localhost:8080/swagger-ui.html
- Documento OpenAPI: http://localhost:8080/v3/api-docs

Os endpoints documentados pelo Swagger sao filtrados para o prefixo `/api/`, conforme a configuracao do projeto.

## Solucao de problemas

### `JAVA_HOME` aponta para outra versao

Confirme que o terminal esta usando JDK 21:

```bash
java -version
```

No Windows, ajuste `JAVA_HOME` para a pasta do JDK 21 e abra um novo terminal antes de executar o Maven Wrapper.

### Falha de conexao com PostgreSQL

Confirme se o container esta ativo:

```bash
docker compose ps
```

Depois confira se o banco, usuario, senha e porta em [application.yml](../src/main/resources/application.yml) correspondem ao PostgreSQL em execucao.

Mensagens como `Connection refused`, `database does not exist` e `password authentication failed` indicam indisponibilidade ou divergencia na configuracao do banco, e nao um problema do Spring Boot.

### Maven Wrapper nao inicia

Execute o comando na pasta `fieldops-api` e verifique se a primeira execucao consegue acessar `repo.maven.apache.org`. O wrapper baixa automaticamente o Maven `3.9.16` e depois as dependencias do projeto.

## Parar o banco

Para parar os containers sem remover os dados:

```bash
docker compose stop
```

Para remover os containers e manter os volumes:

```bash
docker compose down
```

Para remover tambem os dados persistidos, use `docker compose down -v` com cuidado.
