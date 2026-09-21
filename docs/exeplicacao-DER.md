# Documentação Técnica do Banco de Dados — FieldOps

Esta documentação detalha a modelagem relacional do banco de dados da plataforma **FieldOps**.

A arquitetura foi projetada com base nos princípios de normalização (**1FN, 2FN e 3FN**), garantindo:

* **Rastreabilidade**
* **Versionamento de checklists**
* **Auditabilidade**
* **Suporte nativo à operação offline-first**

---

## Visão Geral da Arquitetura de Dados

O banco de dados está dividido em **5 módulos lógicos principais**:

1. **Gestão de Acesso (RBAC):** Controle de usuários, perfis e vínculo de clientes.
2. **Ativos e Locais:** Estrutura geográfica e de equipamentos a serem inspecionados.
3. **Modelagem de Checklists:** Criação e versionamento dos modelos de inspeção.
4. **Execução Operacional:** Agendamento, respostas, evidências e não conformidades.
5. **Auditoria e Sincronização:** Controle de tráfego offline-first e histórico de alterações.

---

# 1. Módulo de Gestão de Acesso

## 1.1 `Perfil`

### Finalidade

Armazena os perfis de acesso do sistema (**modelo RBAC**), definindo os níveis de permissão dos usuários.

### Relacionamentos

* `1 : N` com `Usuarios` — Um perfil pode ser atribuído a vários usuários.

### Dicionário de Colunas

| Coluna        | Tipo        | Restrições        | Descrição                                                                                                                                   |
| ------------- | ----------- | ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| `id`          | INT         | PK, AutoIncrement | Identificador único do perfil.                                                                                                              |
| `nome`        | VARCHAR(50) | NOT NULL          | Nome de exibição do perfil (ex.: *Administrador*, *Supervisor*, *Técnico*, *Cliente*).                                                      |
| `descricao`   | TEXT        | Nullable          | Explicação detalhada das permissões do perfil.                                                                                              |
| `tipo_perfil` | INT         | Unique            | Código numérico fixo utilizado no código (Enum) para controle de acessos. Ex.: `1` = Admin, `2` = Técnico, `3` = Supervisor, `4` = Cliente. |

---

## 1.2 `Usuarios`

### Finalidade

Armazena todos os usuários que acessam a plataforma, incluindo colaboradores internos e clientes.

### Relacionamentos

* `N : 1` com `Perfil` — Vários usuários pertencem a um perfil.
* `N : 1` com `Clientes` — Vários usuários podem pertencer à mesma empresa cliente. Campo opcional para equipe interna.
* `1 : N` com `Atribuicoes` — Como técnico responsável.
* `1 : N` com `Atribuicoes` — Como supervisor responsável.
* `1 : N` com `Modelos_de_Inspecao` — Como criador.
* `1 : N` com `logs_sincronizacao` e `historico_auditoria`.

### Dicionário de Colunas

| Coluna       | Tipo         | Restrições                   | Descrição                                                   |
| ------------ | ------------ | ---------------------------- | ----------------------------------------------------------- |
| `id`         | INT          | PK, AutoIncrement            | Identificador único do usuário.                             |
| `perfil_id`  | INT          | FK → `Perfil.id`             | Referência ao perfil de acesso do usuário.                  |
| `cliente_id` | INT          | FK → `Clientes.id`, Nullable | Referência à empresa cliente, caso seja um usuário externo. |
| `nome`       | VARCHAR(100) | NOT NULL                     | Nome completo do usuário.                                   |
| `email`      | VARCHAR(100) | Unique                       | E-mail utilizado para login e identificação.                |
| `senha_hash` | VARCHAR(255) | NOT NULL                     | Hash seguro da senha de acesso.                             |
| `ativo`      | BOOLEAN      | NOT NULL                     | Indica se o usuário está ativo no sistema.                  |
| `criado_em`  | TIMESTAMP    | NOT NULL                     | Data e hora de cadastro do usuário.                         |

---

# 2. Módulo de Ativos e Locais

## 2.1 `Clientes`

### Finalidade

Armazena as empresas contratantes para as quais as inspeções são realizadas.

### Relacionamentos

* `1 : N` com `Local` — Um cliente possui um ou mais locais/filiais.
* `1 : N` com `Usuarios` — Um cliente pode possuir múltiplos usuários com perfil de visualização/solicitação.

### Dicionário de Colunas

| Coluna         | Tipo         | Restrições        | Descrição                                                    |
| -------------- | ------------ | ----------------- | ------------------------------------------------------------ |
| `id`           | INT          | PK, AutoIncrement | Identificador único do cliente.                              |
| `razao_social` | VARCHAR(150) | NOT NULL          | Nome corporativo ou razão social da empresa.                 |
| `cnpj`         | VARCHAR(20)  | Unique            | Cadastro Nacional da Pessoa Jurídica.                        |
| `contato`      | VARCHAR(100) | NOT NULL          | Nome do responsável principal ou telefone/e-mail de contato. |

---

## 2.2 `Local`

### Finalidade

Representa as unidades físicas, filiais ou áreas geográficas pertencentes a um cliente.

### Relacionamentos

* `N : 1` com `Clientes` — Vários locais pertencem a um único cliente.
* `1 : N` com `Equipamento` — Um local abriga múltiplos equipamentos.

### Dicionário de Colunas

| Coluna       | Tipo         | Restrições         | Descrição                                                               |
| ------------ | ------------ | ------------------ | ----------------------------------------------------------------------- |
| `id`         | INT          | PK, AutoIncrement  | Identificador único do local.                                           |
| `cliente_id` | INT          | FK → `Clientes.id` | Referência ao cliente proprietário da unidade.                          |
| `nome`       | VARCHAR(100) | NOT NULL           | Nome da unidade ou filial. Ex.: *Planta Industrial 1*, *Edifício Sede*. |
| `endereco`   | TEXT         | NOT NULL           | Endereço completo da localização.                                       |

---

## 2.3 `Equipamento`

### Finalidade

Registra os ativos/equipamentos que serão objeto de inspeção técnica.

### Relacionamentos

* `N : 1` com `Local` — Vários equipamentos pertencem a um determinado local.
* `1 : N` com `Atribuicoes` — Um equipamento pode passar por várias inspeções ao longo do tempo.

### Dicionário de Colunas

| Coluna         | Tipo         | Restrições        | Descrição                                                                   |
| -------------- | ------------ | ----------------- | --------------------------------------------------------------------------- |
| `id`           | INT          | PK, AutoIncrement | Identificador único do equipamento.                                         |
| `local_id`     | INT          | FK → `Local.id`   | Referência ao local onde o ativo está instalado.                            |
| `codigo_qr`    | VARCHAR(100) | Unique            | Código único para leitura e identificação via QR Code no aplicativo mobile. |
| `numero_serie` | VARCHAR(100) | NOT NULL          | Número de série de fabricação do equipamento.                               |
| `categoria`    | VARCHAR(50)  | NOT NULL          | Categoria do equipamento. Ex.: *Ar-Condicionado*, *Elevador*, *Gerador*.    |
| `status`       | VARCHAR(30)  | NOT NULL          | Condição do ativo. Ex.: `ATIVO`, `EM_MANUTENCAO`, `INATIVO`.                |

---

# 3. Módulo de Modelagem de Checklists e Versionamento

## 3.1 `Modelos_de_Inspecao`

### Finalidade

Tabela conceitual principal responsável pelo modelo de checklist.

### Relacionamentos

* `N : 1` com `Usuarios` — Um supervisor ou administrador cria o modelo.
* `1 : N` com `Versoes_de_Modelo` — Um modelo possui um histórico de versões.

### Dicionário de Colunas

| Coluna       | Tipo         | Restrições         | Descrição                                                           |
| ------------ | ------------ | ------------------ | ------------------------------------------------------------------- |
| `id`         | INT          | PK, AutoIncrement  | Identificador único do modelo.                                      |
| `criador_id` | INT          | FK → `Usuarios.id` | ID do usuário responsável pela criação.                             |
| `titulo`     | VARCHAR(100) | NOT NULL           | Nome do modelo de checklist. Ex.: *Inspeção Preventiva de Chiller*. |
| `descricao`  | TEXT         | Nullable           | Descrição detalhada sobre a aplicação do modelo.                    |
| `criado_em`  | TIMESTAMP    | NOT NULL           | Data e hora de criação.                                             |

---

## 3.2 `Versoes_de_Modelo`

### Finalidade

Controla o versionamento estrutural dos modelos.

Garante que inspeções antigas mantenham a estrutura de perguntas original da época em que foram executadas.

### Relacionamentos

* `N : 1` com `Modelos_de_Inspecao` — Várias versões pertencem a um modelo.
* `1 : N` com `Itens_modelo` — Uma versão contém um conjunto específico de perguntas.
* `1 : N` com `Atribuicoes` — Uma versão de modelo é utilizada na execução de várias inspeções.

### Dicionário de Colunas

| Coluna          | Tipo        | Restrições                    | Descrição                                                    |
| --------------- | ----------- | ----------------------------- | ------------------------------------------------------------ |
| `id`            | INT         | PK, AutoIncrement             | Identificador único da versão.                               |
| `modelo_id`     | INT         | FK → `Modelos_de_Inspecao.id` | Referência ao modelo pai.                                    |
| `numero_versao` | INT         | NOT NULL                      | Número sequencial da versão. Ex.: `1`, `2`, `3`.             |
| `status`        | VARCHAR(30) | NOT NULL                      | Estado da versão. Ex.: `RASCUNHO`, `ATIVO`, `DESCONTINUADO`. |
| `criado_em`     | TIMESTAMP   | NOT NULL                      | Data e hora em que esta versão foi publicada.                |

---

## 3.3 `Itens_modelo`

### Finalidade

Cadastra as perguntas ou itens individuais que compõem uma versão específica de um checklist.

### Relacionamentos

* `N : 1` com `Versoes_de_Modelo` — Vários itens pertencem a uma versão.
* `1 : N` com `Respostas` — Um item pode ser respondido em múltiplas inspeções.
* `1 : N` com `Nao_Conformidades` — Um item pode ser a origem de uma não conformidade.

### Dicionário de Colunas

| Coluna             | Tipo         | Restrições                  | Descrição                                                                               |
| ------------------ | ------------ | --------------------------- | --------------------------------------------------------------------------------------- |
| `id`               | INT          | PK, AutoIncrement           | Identificador único da pergunta/item.                                                   |
| `versao_modelo_id` | INT          | FK → `Versoes_de_Modelo.id` | Referência à versão à qual a pergunta pertence.                                         |
| `ordem`            | INT          | NOT NULL                    | Sequência de exibição da pergunta na tela do aplicativo.                                |
| `titulo_item`      | VARCHAR(255) | NOT NULL                    | Texto da pergunta ou verificação.                                                       |
| `tipo_resposta`    | VARCHAR(30)  | NOT NULL                    | Formato de entrada exigido no aplicativo. Ex.: `TEXTO`, `BOOLEANO`, `NUMERICO`, `FOTO`. |
| `obrigatorio`      | BOOLEAN      | NOT NULL                    | Define se a resposta é obrigatória para concluir a inspeção.                            |

---

# 4. Módulo de Execução Operacional

## 4.1 `Atribuicoes` — Inspeções

### Finalidade

Representa o agendamento e a execução de uma inspeção em campo.

Utiliza uma chave `UUID` para permitir a criação e geração de registros **offline-first** no aplicativo mobile, reduzindo o risco de conflitos durante a sincronização.

### Relacionamentos

* `N : 1` com `Equipamento` — Inspeção referente a um equipamento.
* `N : 1` com `Versoes_de_Modelo` — Executada com base em uma versão fixa do checklist.
* `N : 1` com `Usuarios` como técnico — Utiliza `tecnico_id`.
* `N : 1` com `Usuarios` como supervisor — Utiliza `supervisor_id`.
* `1 : N` com `Respostas`.
* `1 : N` com `Nao_Conformidades`.
* `1 : N` com `logs_sincronizacao`.

### Dicionário de Colunas

| Coluna                | Tipo          | Restrições                   | Descrição                                                                                                         |
| --------------------- | ------------- | ---------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `id`                  | UUID          | PK                           | Identificador universal único da inspeção, evitando conflitos em operações offline.                               |
| `equipamento_id`      | INT           | FK → `Equipamento.id`        | Equipamento que será inspecionado.                                                                                |
| `versao_modelo_id`    | INT           | FK → `Versoes_de_Modelo.id`  | Versão do checklist utilizada.                                                                                    |
| `tecnico_id`          | INT           | FK → `Usuarios.id`           | Técnico designado para a execução.                                                                                |
| `supervisor_id`       | INT           | FK → `Usuarios.id`, Nullable | Supervisor responsável pela revisão/aprovação.                                                                    |
| `status`              | VARCHAR(30)   | NOT NULL                     | Estado da inspeção. Ex.: `AGENDADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `APROVADA`, `REPROVADA`, `CORRECAO_SOLICITADA`. |
| `data_agendamento`    | TIMESTAMP     | NOT NULL                     | Data e hora previstas para a realização.                                                                          |
| `data_inicio`         | TIMESTAMP     | Nullable                     | Data e hora reais de início da execução.                                                                          |
| `data_conclusao`      | TIMESTAMP     | Nullable                     | Data e hora de finalização em campo.                                                                              |
| `latitude_execucao`   | DECIMAL(10,8) | NOT NULL                     | Latitude GPS capturada no momento do término.                                                                     |
| `longitude_execucao`  | DECIMAL(11,8) | NOT NULL                     | Longitude GPS capturada no momento do término.                                                                    |
| `observacoes_revisao` | TEXT          | Nullable                     | Parecer cadastrado pelo supervisor durante a revisão.                                                             |

---

## 4.2 `Respostas`

### Finalidade

Registra a resposta fornecida pelo técnico para cada item do checklist durante a inspeção.

### Relacionamentos

* `N : 1` com `Atribuicoes` — Pertence a uma inspeção específica.
* `N : 1` com `Itens_modelo` — Refere-se a um item do checklist.
* `1 : N` com `Evidencias` — Uma resposta pode conter zero ou várias fotos.

### Dicionário de Colunas

| Coluna           | Tipo      | Restrições             | Descrição                                                |
| ---------------- | --------- | ---------------------- | -------------------------------------------------------- |
| `id`             | UUID      | PK                     | Identificador único da resposta, gerado offline.         |
| `atribuicao_id`  | UUID      | FK → `Atribuicoes.id`  | Inspeção vinculada.                                      |
| `item_modelo_id` | INT       | FK → `Itens_modelo.id` | Pergunta sendo respondida.                               |
| `valor_resposta` | TEXT      | NOT NULL               | Valor retornado pelo técnico. Ex.: *Sim*, *Não*, *220V*. |
| `observacao`     | TEXT      | Nullable               | Comentários adicionais sobre a resposta.                 |
| `respondido_em`  | TIMESTAMP | NOT NULL               | Timestamp exato do preenchimento no dispositivo mobile.  |

---

## 4.3 `Evidencias` — Fotos

### Finalidade

Armazena os arquivos de mídia e fotografias capturados como comprovação técnica durante uma inspeção.

### Relacionamentos

* `N : 1` com `Respostas` — Vinculada à resposta de um item do checklist.

### Dicionário de Colunas

| Coluna                 | Tipo          | Restrições          | Descrição                                                      |
| ---------------------- | ------------- | ------------------- | -------------------------------------------------------------- |
| `id`                   | UUID          | PK                  | Identificador único da evidência.                              |
| `resposta_inspecao_id` | UUID          | FK → `Respostas.id` | Resposta associada.                                            |
| `caminho_arquivo`      | VARCHAR(255)  | NOT NULL            | URI ou caminho do arquivo de imagem salvo no servidor/storage. |
| `latitude`             | DECIMAL(10,8) | Nullable            | Coordenada GPS de onde a foto foi tirada.                      |
| `longitude`            | DECIMAL(11,8) | Nullable            | Coordenada GPS de onde a foto foi tirada.                      |
| `criado_em`            | TIMESTAMP     | NOT NULL            | Data e hora da captura.                                        |

---

## 4.4 `Nao_Conformidades`

### Finalidade

Registra falhas, defeitos ou desvios de padrão detectados durante a verificação.

### Relacionamentos

* `N : 1` com `Atribuicoes` — Associada à execução da inspeção.
* `N : 1` com `Itens_modelo` — Associada ao item específico onde a falha foi apontada.

### Dicionário de Colunas

| Coluna               | Tipo        | Restrições             | Descrição                                                        |
| -------------------- | ----------- | ---------------------- | ---------------------------------------------------------------- |
| `id`                 | UUID        | PK                     | Identificador único do registro de não conformidade.             |
| `atribuicao_id`      | UUID        | FK → `Atribuicoes.id`  | Inspeção na qual o problema foi detectado.                       |
| `item_modelo_id`     | INT         | FK → `Itens_modelo.id` | Item do checklist que apresentou a falha.                        |
| `descricao_problema` | TEXT        | NOT NULL               | Relatório técnico detalhando a falha encontrada.                 |
| `severidade`         | VARCHAR(30) | NOT NULL               | Gravidade do problema. Ex.: `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`. |
| `status_resolucao`   | VARCHAR(30) | NOT NULL               | Estado da correção. Ex.: `PENDENTE`, `EM_CORRECAO`, `RESOLVIDO`. |

---

# 5. Módulo de Auditoria e Sincronização

## 5.1 `logs_sincronizacao`

### Finalidade

Rastreia o fluxo de sincronização **Offline-to-Online**, registrando quando os dados gravados localmente no aplicativo mobile foram recebidos pelo servidor.

### Relacionamentos

* `N : 1` com `Atribuicoes` — Registra o envio de uma inspeção.
* `N : 1` com `Usuarios` — Usuário que realizou o upload.

### Dicionário de Colunas

| Coluna                 | Tipo         | Restrições            | Descrição                                                                       |
| ---------------------- | ------------ | --------------------- | ------------------------------------------------------------------------------- |
| `id`                   | BIGINT       | PK, AutoIncrement     | Identificador único do log.                                                     |
| `atribuicao_id`        | UUID         | FK → `Atribuicoes.id` | Inspeção sincronizada.                                                          |
| `usuario_id`           | INT          | FK → `Usuarios.id`    | Usuário que disparou a sincronização.                                           |
| `dispositivo_uuid`     | VARCHAR(255) | NOT NULL              | Identificador do hardware/aplicativo do celular que enviou os dados.            |
| `data_geracao_offline` | TIMESTAMP    | NOT NULL              | Momento em que a inspeção foi finalizada localmente sem conexão com a internet. |
| `data_sincronizacao`   | TIMESTAMP    | NOT NULL              | Momento em que o servidor recebeu o payload com sucesso.                        |
| `status_sync`          | VARCHAR(30)  | NOT NULL              | Estado do processamento. Ex.: `SUCESSO`, `CONFLITO`, `ERRO_VALIDACAO`.          |

---

## 5.2 `historico_auditoria`

### Finalidade

Tabela global de rastreabilidade do sistema.

Registra alterações estruturais ou operacionais, como **criação, alteração e exclusão**, para fins de auditoria e compliance.

### Relacionamentos

* `N : 1` com `Usuarios` — Usuário responsável pela alteração.

### Dicionário de Colunas

| Coluna             | Tipo              | Restrições         | Descrição                                                   |
| ------------------ | ----------------- | ------------------ | ----------------------------------------------------------- |
| `id`               | BIGINT            | PK, AutoIncrement  | Identificador único da auditoria.                           |
| `usuario_id`       | INT               | FK → `Usuarios.id` | Usuário responsável pela ação.                              |
| `entidade_afetada` | VARCHAR(100)      | NOT NULL           | Nome da tabela alterada. Ex.: `EQUIPAMENTO`, `ATRIBUICOES`. |
| `entidade_id`      | VARCHAR(36) / INT | NOT NULL           | ID (PK) da linha específica que sofreu a alteração.         |
| `acao`             | VARCHAR(20)       | NOT NULL           | Operação realizada. Ex.: `INSERT`, `UPDATE`, `DELETE`.      |
| `dados_anteriores` | JSON / TEXT       | Nullable           | Estado do registro antes da alteração.                      |
| `dados_novos`      | JSON / TEXT       | Nullable           | Novo estado do registro após a alteração.                   |
| `criado_em`        | TIMESTAMP         | NOT NULL           | Data e hora exatas do evento.                               |

---

# Matriz Resumida de Relacionamentos

| Tabela Origem           | Tabela Destino        | Cardinalidade | Campo Chave Estrangeira (FK)       |
| ----------------------- | --------------------- | ------------- | ---------------------------------- |
| `Perfil`                | `Usuarios`            | `1 : N`       | `Usuarios.perfil_id`               |
| `Clientes`              | `Usuarios`            | `1 : N`       | `Usuarios.cliente_id`              |
| `Clientes`              | `Local`               | `1 : N`       | `Local.cliente_id`                 |
| `Local`                 | `Equipamento`         | `1 : N`       | `Equipamento.local_id`             |
| `Modelos_de_Inspecao`   | `Versoes_de_Modelo`   | `1 : N`       | `Versoes_de_Modelo.modelo_id`      |
| `Versoes_de_Modelo`     | `Itens_modelo`        | `1 : N`       | `Itens_modelo.versao_modelo_id`    |
| `Versoes_de_Modelo`     | `Atribuicoes`         | `1 : N`       | `Atribuicoes.versao_modelo_id`     |
| `Equipamento`           | `Atribuicoes`         | `1 : N`       | `Atribuicoes.equipamento_id`       |
| `Usuarios` (Técnico)    | `Atribuicoes`         | `1 : N`       | `Atribuicoes.tecnico_id`           |
| `Usuarios` (Supervisor) | `Atribuicoes`         | `1 : N`       | `Atribuicoes.supervisor_id`        |
| `Atribuicoes`           | `Respostas`           | `1 : N`       | `Respostas.atribuicao_id`          |
| `Itens_modelo`          | `Respostas`           | `1 : N`       | `Respostas.item_modelo_id`         |
| `Respostas`             | `Evidencias`          | `1 : N`       | `Evidencias.resposta_inspecao_id`  |
| `Atribuicoes`           | `Nao_Conformidades`   | `1 : N`       | `Nao_Conformidades.atribuicao_id`  |
| `Itens_modelo`          | `Nao_Conformidades`   | `1 : N`       | `Nao_Conformidades.item_modelo_id` |
| `Atribuicoes`           | `logs_sincronizacao`  | `1 : N`       | `logs_sincronizacao.atribuicao_id` |
| `Usuarios`              | `historico_auditoria` | `1 : N`       | `historico_auditoria.usuario_id`   |

---

# Considerações Finais

A estrutura proposta para o banco de dados do **FieldOps** foi organizada para suportar o ciclo completo de uma inspeção técnica, desde o cadastro de clientes, locais e equipamentos até a execução da inspeção, registro de respostas, evidências e não conformidades.

Os principais pontos arquiteturais são:

* **RBAC:** controle estruturado de permissões por perfil.
* **Versionamento:** cada inspeção mantém referência à versão específica do checklist utilizada.
* **Offline-first:** utilização de `UUID` nas entidades operacionais para evitar conflitos durante a criação de registros sem conexão.
* **Rastreabilidade:** registros de sincronização permitem acompanhar o fluxo de dados entre dispositivo e servidor.
* **Auditoria:** alterações importantes ficam registradas no histórico do sistema.
* **Normalização:** separação das entidades e relacionamentos para reduzir redundância e manter a integridade dos dados.
* **Escalabilidade:** a separação modular permite evolução independente das diferentes áreas do sistema.
