package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.EquipamentoService;
import br.com.fieldops.api.presentation.dto.EquipamentoRequestDTO;
import br.com.fieldops.api.presentation.dto.EquipamentoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipamentos")
@Tag(name = "Equipamentos", description = "Endpoints para gerenciamento de equipamentos e identificação por QR Code")
@SecurityRequirement(name = "bearer-key")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @PostMapping
    @Operation(summary = "Cadastrar novo equipamento com QR Code automático ou customizado")
    public ResponseEntity<EquipamentoResponseDTO> criar(@RequestBody @Valid EquipamentoRequestDTO dto) {
        EquipamentoResponseDTO response = equipamentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar e pesquisar equipamentos com paginação e filtros")
    public ResponseEntity<Page<EquipamentoResponseDTO>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Long localId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(equipamentoService.listarPaginado(termo, localId, pageable));
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os equipamentos ativos sem paginação")
    public ResponseEntity<List<EquipamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/local/{localId}")
    @Operation(summary = "Listar equipamentos ativos vinculados a um local (seleção encadeada)")
    public ResponseEntity<List<EquipamentoResponseDTO>> listarPorLocal(@PathVariable Long localId) {
        return ResponseEntity.ok(equipamentoService.listarPorLocal(localId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar equipamento por ID")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id));
    }

    @GetMapping("/qrcode/{codigoQr}")
    @Operation(summary = "Identificar equipamento a partir do código QR único")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorQrCode(@PathVariable String codigoQr) {
        return ResponseEntity.ok(equipamentoService.buscarPorQrCode(codigoQr));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um equipamento")
    public ResponseEntity<EquipamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EquipamentoRequestDTO dto) {
        return ResponseEntity.ok(equipamentoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir/inativar equipamento")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        equipamentoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
