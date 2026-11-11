package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.LocalService;
import br.com.fieldops.api.presentation.dto.LocalRequestDTO;
import br.com.fieldops.api.presentation.dto.LocalResponseDTO;
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
@RequestMapping("/api/v1/locais")
@Tag(name = "Locais", description = "Endpoints para gerenciamento de locais de atendimento")
@SecurityRequirement(name = "bearer-key")
public class LocalController {

    @Autowired
    private LocalService localService;

    @PostMapping
    @Operation(summary = "Cadastrar novo local")
    public ResponseEntity<LocalResponseDTO> criar(@RequestBody @Valid LocalRequestDTO dto) {
        LocalResponseDTO response = localService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar e pesquisar locais com paginação e filtros")
    public ResponseEntity<Page<LocalResponseDTO>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(localService.listarPaginado(termo, clienteId, pageable));
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os locais ativos sem paginação")
    public ResponseEntity<List<LocalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(localService.listarTodos());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar locais ativos vinculados a um cliente (seleção encadeada)")
    public ResponseEntity<List<LocalResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(localService.listarPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar local por ID")
    public ResponseEntity<LocalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um local")
    public ResponseEntity<LocalResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid LocalRequestDTO dto) {
        return ResponseEntity.ok(localService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir/inativar local respeitando dependências")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        localService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
