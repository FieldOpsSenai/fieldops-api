# Atualização: Modelos, Evidências, Execução e Revisão de Inspeções

## Resumo das Alterações

### 1. Modelos de Inspeção e Foto Obrigatória
* **Preservação de `evidenciaObrigatoria` no versionamento:** Corrigido `ModeloInspecaoService.criarNovaVersao` para copiar o campo `evidenciaObrigatoria` dos itens ao gerar uma nova versão de modelo.
* **Tratamento de conflito de estado (`409 Conflict`):** Adicionado `@ExceptionHandler(IllegalStateException.class)` no `GlobalExceptionHandler` para retornar `409 Conflict` com mensagem estruturada em JSON em vez de `500 Internal Server Error`.

### 2. Execução e Evidências (`/snapshot`, `/respostas` e `/fotos`)
* **Leitura de respostas para gestores:** Atualizado `RespostaInspecaoService.listar` (`GET /api/v1/inspecoes/{inspecaoId}/respostas`) para permitir leitura por `ADMINISTRADOR`, `SUPERVISOR` e pelo `TECNICO` atribuído.
* **Bloqueio de edição após conclusão/cancelamento:** `RespostaInspecaoService.salvar` agora impede alteração de respostas quando a inspeção está `CONCLUIDA` ou `CANCELADA`.
* **Visualização da foto pelo gestor:**
  * Adicionado endpoint `GET /api/v1/inspecoes/{inspecaoId}/fotos/{fotoId}/arquivo` em `FotoInspecaoController` (`FotoInspecaoService.carregarArquivo`) servindo a imagem inline para `ADMINISTRADOR`, `SUPERVISOR` e o `TECNICO` atribuído.
  * Adicionado campo `urlArquivo` em `FotoInspecaoResponseDTO`.

### 3. Revisão (`ADMINISTRADOR` e `SUPERVISOR`), Persistência e Reenvio
* **Revisor pela sessão autenticada e suporte a `ADMINISTRADOR`:**
  * `InspecaoService.revisarInspecao` (`PATCH /api/v1/inspecoes/{id}/revisar`) agora permite `ADMINISTRADOR` e `SUPERVISOR` (via `exigirPlanejador()`) e identifica o revisor automaticamente pela sessão autenticada (`usuarioAutenticado()`), removendo o bug de comparação entre `String` e enum `Perfil`.
  * `supervisorId` em `InspecaoRevisaoDTO` passou a ser opcional.
* **Persistência da decisão e distinção entre encerramento e aprovação:**
  * Adicionado campo `revisaoAprovada` (`Boolean`) na entidade `Inspecao` e criada a migration Flyway `V5__adicionar_revisao_aprovada.sql`.
  * `InspecaoResponseDTO` agora retorna `modeloTitulo`, `supervisorId`, `supervisorNome`, `dataRevisao`, `revisaoAprovada` (`true` = aprovada, `false` = reprovada, `null` = aguardando revisão) e `motivoReprovacao`.
  * Adicionado endpoint `GET /api/v1/inspecoes/{id}` em `InspecaoController` para recarregamento individual da inspeção.
* **Correção e reenvio pelo técnico:**
  * Quando reprovada (`aprovado = false`), a inspeção retorna para `EM_ANDAMENTO` com `revisaoAprovada = false` e `motivoReprovacao`.
  * Ao concluir/reenviar (`POST /api/v1/inspecoes/{id}/concluir`), `revisaoAprovada`, `dataRevisao` e `motivoReprovacao` são limpos para que a inspeção volte ao estado de aguardando revisão.

## Arquivos Alterados / Adicionados
* `src/main/java/br/com/fieldops/api/domain/entity/Inspecao.java`
* `src/main/java/br/com/fieldops/api/domain/service/ModeloInspecaoService.java`
* `src/main/java/br/com/fieldops/api/domain/service/RespostaInspecaoService.java`
* `src/main/java/br/com/fieldops/api/domain/service/FotoInspecaoService.java`
* `src/main/java/br/com/fieldops/api/domain/service/InspecaoService.java`
* `src/main/java/br/com/fieldops/api/presentation/controller/FotoInspecaoController.java`
* `src/main/java/br/com/fieldops/api/presentation/controller/InspecaoController.java`
* `src/main/java/br/com/fieldops/api/presentation/dto/FotoInspecaoResponseDTO.java`
* `src/main/java/br/com/fieldops/api/presentation/dto/InspecaoResponseDTO.java`
* `src/main/java/br/com/fieldops/api/presentation/dto/InspecaoRevisaoDTO.java`
* `src/main/java/br/com/fieldops/api/presentation/exception/GlobalExceptionHandler.java`
* `src/main/resources/db/migration/V5__adicionar_revisao_aprovada.sql`

