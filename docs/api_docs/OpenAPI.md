# 📄 Especificação OpenAPI & Dados Simulados (PBI-006)

Esta documentação descreve a estratégia de contrato de API via OpenAPI (Swagger UI) e disponibilização de esquemas de dados para desenvolvimento paralelo dos times Frontend (Mobile e Admin).

## 🔗 Endpoints da Documentação
Com a aplicação FieldOps em execução local (`./mvnw spring-boot:run` ou pela IDE):

- **Swagger UI (Interface Interativa):** `http://localhost:8080/swagger-ui.html`
- **Especificação JSON (Contrato OpenAPI v3):** `http://localhost:8080/v3/api-docs`

## 📑 Versionamento e Padrões de Contrato
- **Versionamento Base:** Todas as rotas de negócio e autenticação do sistema utilizam o prefixo `/api/v1/...`.
- **Modelagem de Dados:** Todos os DTOs de Request e Response contam com anotações `@Schema` (`io.swagger.v3`) fornecendo descrições funcionais e valores de exemplo (`example`).
- **Respostas de Erro:** As respostas em cenários de falha (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`) utilizam o contrato padronizado `ErroResponseDTO`.

## 🎭 Estratégia de Mocks e Integração Frontend
1. **Consumo Interativo:** Os desenvolvedores Frontend podem utilizar a interface do Swagger UI local para validar os corpos das requisições e testar os endpoints ativamente.
2. **Exportação de Mocks:** Caso o time de Frontend (Mobile/Admin) prefira rodar um servidor de mock estático em suas máquinas sem subir o banco de dados PostgreSQL:
   - Acessar `http://localhost:8080/v3/api-docs`.
   - Salvar o arquivo JSON gerado.
   - Importar o arquivo no **Postman**, **Stoplight Prism** ou **MSW (Mock Service Worker)** para simulação instantânea das respostas.

## 🔄 Fluxo de Atualização do Contrato
Sempre que uma nova PBI alterar um DTO ou Controller:
1. Atualizar as anotações `@Schema` nos atributos alterados do DTO.
2. Validar se os códigos HTTP no `@ApiResponses` do Controller continuam coerentes.
3. Notificar os integradores dos times Mobile e Admin sobre a atualização do contrato.