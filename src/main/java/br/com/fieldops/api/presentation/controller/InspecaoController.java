package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.InspecaoService;
import br.com.fieldops.api.domain.service.InspecaoSnapshotService;
import br.com.fieldops.api.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inspecoes")
@Tag(name = "Inspeções", description = "Endpoints para agendamento, acompanhamento e execução de inspeções")
@SecurityRequirement(name = "bearer-key")
public class InspecaoController {

    @Autowired
    private InspecaoService inspecaoService;

    @Autowired
    private InspecaoSnapshotService snapshotService;

    @PostMapping
    @Operation(summary = "Criar/agendar nova inspeção", description = "Cria uma inspeção a partir de um modelo publicado e gera o snapshot imutável")
    public ResponseEntity<InspecaoResponseDTO> criar(@RequestBody @Valid InspecaoRequestDTO dto) {
        InspecaoResponseDTO response = inspecaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar inspeções com pesquisa, filtros combináveis e paginação")
    public ResponseEntity<Page<InspecaoResponseDTO>> listarTodas(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) br.com.fieldops.api.domain.entity.StatusInspecao status,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) br.com.fieldops.api.domain.enums.PrioridadeInspecao prioridade,
            @PageableDefault(size = 10, sort = "dataAgendada") Pageable pageable) {
        return ResponseEntity.ok(inspecaoService.pesquisar(termo, status, usuarioId, clienteId, prioridade, pageable));
    }

    @GetMapping("/tecnicos-disponiveis")
    @Operation(summary = "Listar técnicos ativos disponíveis para atribuição")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTecnicosAtivos() {
        return ResponseEntity.ok(inspecaoService.listarTecnicosAtivos());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar inspeções por técnico")
    public ResponseEntity<List<InspecaoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(inspecaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/minhas")
    @Operation(summary = "Listar inspeções atribuídas ao técnico autenticado")
    public ResponseEntity<List<InspecaoResponseDTO>> listarMinhas() {
        return ResponseEntity.ok(inspecaoService.listarMinhasInspecoes());
    }

    @GetMapping("/minhas/{id}")
    @Operation(summary = "Baixar os dados de uma inspeção atribuída ao técnico autenticado")
    public ResponseEntity<InspecaoResponseDTO> buscarMinha(@PathVariable Long id) {
        return ResponseEntity.ok(inspecaoService.buscarMinhaInspecao(id));
    }

    @PostMapping("/{id}/confirmar-equipamento")
    @Operation(summary = "Confirmar o equipamento da inspeção pela leitura do QR Code")
    public ResponseEntity<ConfirmacaoEquipamentoResponseDTO> confirmarEquipamento(
            @PathVariable Long id, @RequestBody @Valid ConfirmacaoEquipamentoDTO dto) {
        return ResponseEntity.ok(inspecaoService.confirmarEquipamento(id, dto.getCodigoQr()));
    }

    @GetMapping("/{id}/snapshot")
    @Operation(summary = "Recuperar snapshot imutável do checklist da inspeção", description = "PBI-024: Retorna os itens de checklist congelados no momento do agendamento")
    public ResponseEntity<List<InspecaoSnapshotItemResponseDTO>> recuperarSnapshot(@PathVariable Long id) {
        return ResponseEntity.ok(snapshotService.recuperarSnapshot(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da inspeção")
    public ResponseEntity<InspecaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid InspecaoStatusDTO dto) {
        
        InspecaoResponseDTO response = inspecaoService.atualizarStatus(id, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/concluir")
    @Operation(summary = "Concluir inspeção após validar respostas obrigatórias e evidências")
    public ResponseEntity<InspecaoResponseDTO> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(inspecaoService.concluir(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar inspeção", description = "Cancela uma inspeção pendente ou agendada informando um motivo obrigatório")
    public ResponseEntity<InspecaoResponseDTO> cancelarInspecao(
            @PathVariable Long id,
            @RequestBody @Valid InspecaoCancelamentoDTO dto) {
        
        InspecaoResponseDTO response = inspecaoService.cancelarInspecao(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/planejamento")
    @Operation(summary = "Atualizar prioridade, prazo e instruções da inspeção")
    public ResponseEntity<InspecaoResponseDTO> atualizarPlanejamento(
            @PathVariable Long id,
            @RequestBody @Valid InspecaoPlanejamentoUpdateDTO dto) {
        return ResponseEntity.ok(inspecaoService.atualizarPlanejamento(id, dto));
    }

    @PatchMapping("/{id}/revisar")
    @Operation(summary = "Revisar inspeção", description = "Permite ao supervisor aprovar ou reprovar uma inspeção realizada")
    public ResponseEntity<InspecaoResponseDTO> revisarInspecao(
            @PathVariable Long id,
            @Valid @RequestBody InspecaoRevisaoDTO dto) {
        InspecaoResponseDTO response = inspecaoService.revisarInspecao(id, dto);
        return ResponseEntity.ok(response);
    }
}
