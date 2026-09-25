# Guia de Execução do Banco de Dados FieldOps via Docker

Este guia contém as instruções passo a passo para subir e gerenciar o ambiente de banco de dados PostgreSQL utilizando **Docker Compose** localmente.

---

## Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [Git](https://git-scm.com/)

---

## Estrutura de Arquivos

Certifique-se de que os arquivos de configuração estejam dispostos na raiz do projeto:

```text
FieldOps-Plataforma-de-Inspecao/
├── docker-compose.yml
├── init_fieldops.sql
├── pom.xml
└── ...
```

---

## Passo a Passo para Subir o Ambiente

### 1. Iniciar o Banco de Dados

Abra o terminal na pasta raiz do projeto e execute o comando abaixo para criar e rodar os containers em segundo plano (`-d`):

```bash
docker-compose up -d
```

> **Nota:** Na primeira vez que este comando for executado, o PostgreSQL irá ler automaticamente o arquivo `init_fieldops.sql` e criar todas as 12 tabelas, chaves estrangeiras e a extensão de `UUID`.

---

### 2. Verificar se o Container está Rodando

Para checar o status dos containers e verificar as portas mapeadas, execute:

```bash
docker ps
```

Você deverá ver os containers:

* `fieldops-db` — PostgreSQL
* `fieldops-pgadmin` — pgAdmin

Ambos devem apresentar o status **Up**.

---

## Acessando a Interface Visual (pgAdmin)

Se você incluiu o serviço do **pgAdmin** no seu `docker-compose.yml`, siga o procedimento abaixo para visualizar as tabelas.

### 1. Acessar o pgAdmin

Acesse o navegador no endereço:

```text
http://localhost:5050
```

### 2. Fazer Login

Utilize as credenciais padrão:

| Campo      | Valor             |
| ---------- | ----------------- |
| **E-mail** | `admin@admin.com` |
| **Senha**  | `admin`           |

### 3. Registrar o Servidor

No pgAdmin:

1. Clique com o botão direito em **Servers**.

2. Selecione **Register** > **Server...**.

3. Na aba **General**, defina:

   **Name:**

   ```text
   FieldOps Local
   ```

4. Na aba **Connection**, preencha:

   | Campo                    | Valor               |
   | ------------------------ | ------------------- |
   | **Host name/address**    | `postgres`          |
   | **Port**                 | `5432`              |
   | **Maintenance database** | `fieldops`          |
   | **Username**             | `fieldops_user`     |
   | **Password**             | `fieldops_password` |

   > **Nota:** O valor `postgres` corresponde ao nome do serviço do PostgreSQL dentro da rede do Docker Compose.

5. Clique em **Save**.

O banco de dados estará acessível pelo caminho:

```text
Databases
└── fieldops
    └── Schemas
        └── public
            └── Tables
```

---

## Conexão com o Backend Java (Spring Boot)

Adicione as seguintes configurações no arquivo:

```text
src/main/resources/application.properties
```

### Configuração do PostgreSQL

```properties
# Configuração de Conexão com o PostgreSQL Local
spring.datasource.url=jdbc:postgresql://localhost:5432/fieldops
spring.datasource.username=fieldops_user
spring.datasource.password=fieldops_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações do Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> **Importante:** O `ddl-auto=validate` faz com que o Hibernate apenas valide se as entidades Java são compatíveis com a estrutura existente do banco, sem criar ou alterar tabelas automaticamente.

---

## Comandos Úteis de Manutenção

### Parar os Containers

Para parar os containers sem apagar os dados:

```bash
docker-compose stop
```

---

### Iniciar os Containers Novamente

Para iniciar containers que foram parados:

```bash
docker-compose start
```

---

### Remover os Containers

Para remover os containers, mantendo os volumes e os dados:

```bash
docker-compose down
```

---

### Resetar Completamente o Banco

Para remover os containers **e os volumes de dados**, apagando completamente o banco:

```bash
docker-compose down -v
```

> **Atenção:** Este comando apaga os volumes associados ao Docker Compose. Os dados armazenados no PostgreSQL serão perdidos.

---

## Resumo dos Comandos

| Comando                  | Função                                         |
| ------------------------ | ---------------------------------------------- |
| `docker-compose up -d`   | Cria e inicia os containers em segundo plano   |
| `docker-compose stop`    | Para os containers sem remover dados           |
| `docker-compose start`   | Inicia containers que estavam parados          |
| `docker-compose down`    | Remove os containers e mantém os volumes       |
| `docker-compose down -v` | Remove containers e volumes, apagando os dados |
| `docker ps`              | Lista os containers em execução                |

---

## Arquitetura do Ambiente

O ambiente local do **FieldOps** é composto pelos seguintes serviços:

```text
┌──────────────────────┐
│     Spring Boot      │
│       Backend        │
└──────────┬───────────┘
           │
           │ JDBC
           ▼
┌──────────────────────┐
│      PostgreSQL      │
│     fieldops-db      │
│      Port: 5432      │
└──────────┬───────────┘
           │
           │
           ▼
┌──────────────────────┐
│        pgAdmin       │
│    Interface Web     │
│      Port: 5050      │
└──────────────────────┘
```

> **Fluxo:** O backend Spring Boot se conecta ao PostgreSQL através da porta `5432`, enquanto o pgAdmin fornece uma interface web para administração e visualização do banco de dados.
