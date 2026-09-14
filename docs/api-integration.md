# Guia de Integração - FieldOps REST API

Este documento fornece a especificação atualizada da API para o desenvolvimento e integração das aplicações Web e Mobile.

---

## 1. Visão Geral de Arquitetura & Regras

* **URL Base:** `http://localhost:8080/api/v1` (ou via Swagger UI em `http://localhost:8080/swagger-ui.html`)
* **Padrão REST:** Retornos em JSON (`application/json`).
* **Regra de Perfis (`Perfil`):** `ADMINISTRADOR`, `SUPERVISOR`, `TECNICO`.
* **Regra de Atribuição:** Apenas usuários com perfil **`TECNICO`** podem ser atribuídos como responsáveis por uma inspeção no payload de criação.

---

## 2. Usuários (`/usuarios`)

### **POST /usuarios** — Cadastrar Usuário
Cria um novo usuário na plataforma. O campo `perfil` aceita os valores: `ADMINISTRADOR`, `SUPERVISOR` ou `TECNICO`.

* **Headers:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "nome": "Carlos Silva",
  "email": "carlos.tecnico@fieldops.com",
  "senha": "senhaSegura123",
  "perfil": "TECNICO"
}

Response (201 Created):

{
  "id": 3,
  "nome": "Carlos Silva",
  "email": "carlos.tecnico@fieldops.com",
  "perfil": "TECNICO",
  "ativo": true
}

```

## 3. Clientes (`/clientes`)
POST /clientes — Cadastrar Cliente
Cria um novo cliente no sistema.

Headers: Content-Type: application/json

Request Body:

```json

{
  "nome": "Indústria Metalúrgica Silva",
  "cnpj": "12345678000195",
  "telefone": "11999998888",
  "email": "contato@metalurgicasilva.com.br"
}

Response (201 Created):

{
  "id": 2,
  "nome": "Indústria Metalúrgica Silva",
  "cnpj": "12345678000195",
  "telefone": "11999998888",
  "email": "contato@metalurgicasilva.com.br",
  "ativo": true
}

```

## 4. Locais (`/locais`)
POST /locais — Cadastrar Unidade/Local
Vincula uma unidade física ou planta operacional a um cliente existente.

Headers: Content-Type: application/json

Request Body:

```json

{
  "nome": "Galpão Principal - Almoxarifado",
  "endereco": "Rua das Indústrias, 500 - Distrito Industrial",
  "clienteId": 2
}

Response (201 Created):

{
  "id": 2,
  "nome": "Galpão Principal - Almoxarifado",
  "endereco": "Rua das Indústrias, 500 - Distrito Industrial",
  "clienteId": 2,
  "clienteNome": "Indústria Metalúrgica Silva",
  "ativo": true
}

```

## 5. Equipamentos (`/equipamentos`)
POST /equipamentos — Cadastrar Maquinário
Cadastra um equipamento associado a um local. O campo ativo é definido automaticamente como true por padrão no backend.

Headers: Content-Type: application/json

Request Body:

```json

{
  "nome": "Compressor de Ar Parafuso 50HP",
  "numeroSerie": "CMP-998877",
  "tipo": "Compressor",
  "localId": 2
}

Response (201 Created):

{
  "id": 2,
  "nome": "Compressor de Ar Parafuso 50HP",
  "numeroSerie": "CMP-998877",
  "tipo": "Compressor",
  "ativo": true,
  "localId": 2,
  "localNome": "Galpão Principal - Almoxarifado"
}

```

## 6. Inspeções (`/inspecoes`)
POST /inspecoes — Agendar Inspeção
Cria e atribui uma ordem de inspeção. O usuarioId deve obrigatoriamente pertencer a um usuário com perfil TECNICO.

Headers: Content-Type: application/json

Request Body:

```json

{
  "descricao": "Inspeção Preventiva Trimestral",
  "dataAgendada": "2026-08-25T09:00:00",
  "observacoes": "Verificar níveis de óleo e filtros",
  "equipamentoId": 2,
  "usuarioId": 3
}

Response (201 Created):

{
  "id": 1,
  "descricao": "Inspeção Preventiva Trimestral",
  "status": "PENDENTE",
  "dataAgendada": "2026-08-25T09:00:00",
  "dataRealizacao": null,
  "observacoes": "Verificar níveis de óleo e filtros",
  "equipamentoId": 2,
  "equipamentoNome": "Compressor de Ar Parafuso 50HP",
  "usuarioId": 3,
  "usuarioNome": "Carlos Silva"
}

```

## PATCH /inspecoes/{id}/status — Concluir/Atualizar Status
Atualiza o status da inspeção e grava automaticamente a data e hora oficial de realização no fuso horário America/Sao_Paulo.

Path Parameter: id (integer) — Ex: 1

Headers: Content-Type: application/json

Request Body:

```json

{
  "status": "CONCLUIDA",
  "observacoes": "Inspeção realizada com sucesso. Filtros limpos e óleo trocado."
}

Response (200 OK):

{
  "id": 1,
  "descricao": "Inspeção Preventiva Trimestral",
  "status": "CONCLUIDA",
  "dataAgendada": "2026-08-25T09:00:00",
  "dataRealizacao": "2026-08-24T11:44:00",
  "observacoes": "Inspeção realizada com sucesso. Filtros limpos e óleo trocado.",
  "equipamentoId": 2,
  "equipamentoNome": "Compressor de Ar Parafuso 50HP",
  "usuarioId": 3,
  "usuarioNome": "Carlos Silva"
}

```