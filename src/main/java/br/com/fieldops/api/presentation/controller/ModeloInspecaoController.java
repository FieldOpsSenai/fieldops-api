package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.service.ModeloInspecaoService;
import br.com.fieldops.api.presentation.dto.ModeloInspecaoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos-inspecao")
@Tag(name = "Modelos de Inspeção", description = "Endpoints para gerenciamento e listagem de modelos de checklists")
public class ModeloInspecaoController {

    private final ModeloInspecaoService modeloInspecaoService;

    public ModeloInspecaoController(ModeloInspecaoService modeloInspecaoService) {
        this.modeloInspecaoService = modeloInspecaoService;
    }

    @GetMapping
    @Operation(summary = "Listar modelos publicados", description = "Retorna todos os modelos de inspeção com status PUBLICADO disponíveis para uso")
    public ResponseEntity<List<ModeloInspecao>> listarPublicados() {
        List<ModeloInspecao> modelos = modeloInspecaoService.listarModelosPublicados();
        return ResponseEntity.ok(modelos);
    }

    @PostMapping
    @Operation(summary = "Criar modelo de inspeção", description = "Cria um novo modelo de inspeção em estado de RASCUNHO com suas seções e itens")
    public ResponseEntity<ModeloInspecao> criarModelo(@RequestBody @Valid ModeloInspecaoRequestDTO dto) {
        ModeloInspecao novoModelo = modeloInspecaoService.criarModelo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoModelo);
    }

    @PatchMapping("/{id}/publicar")
    @Operation(summary = "Publicar modelo de inspeção", description = "Altera o status de um modelo de RASCUNHO para PUBLICADO")
    public ResponseEntity<ModeloInspecao> publicarModelo(@PathVariable Long id) {
        ModeloInspecao modeloPublicado = modeloInspecaoService.publicarModelo(id);
        return ResponseEntity.ok(modeloPublicado);
    }
}