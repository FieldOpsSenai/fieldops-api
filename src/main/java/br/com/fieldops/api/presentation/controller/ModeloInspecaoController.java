package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.entity.ItemInspecao;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.SecaoInspecao;
import br.com.fieldops.api.domain.service.ModeloInspecaoService;
import br.com.fieldops.api.presentation.dto.ItemInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.ModeloInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.SecaoInspecaoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos-inspecao")
@Tag(name = "Modelos de Inspeção", description = "Endpoints para gerenciamento de checklists, seções, itens e versionamento imutável")
@SecurityRequirement(name = "bearer-key")
public class ModeloInspecaoController {

    private final ModeloInspecaoService modeloInspecaoService;

    public ModeloInspecaoController(ModeloInspecaoService modeloInspecaoService) {
        this.modeloInspecaoService = modeloInspecaoService;
    }

    @PostMapping
    @Operation(summary = "Criar modelo de inspeção em rascunho", description = "Inicia a configuração de um novo checklist em estado RASCUNHO")
    public ResponseEntity<ModeloInspecao> criarModelo(@RequestBody @Valid ModeloInspecaoRequestDTO dto) {
        ModeloInspecao novoModelo = modeloInspecaoService.criarModelo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoModelo);
    }

    @GetMapping
    @Operation(summary = "Listar modelos com paginação e filtro por termo e status")
    public ResponseEntity<Page<ModeloInspecao>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10, sort = "dataCriacao") Pageable pageable) {
        return ResponseEntity.ok(modeloInspecaoService.listarPaginado(termo, status, pageable));
    }

    @GetMapping("/publicados")
    @Operation(summary = "Listar todos os modelos publicados disponíveis para agendamento")
    public ResponseEntity<List<ModeloInspecao>> listarPublicados() {
        return ResponseEntity.ok(modeloInspecaoService.listarModelosPublicados());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Visualizar detalhes de um modelo de inspeção por ID")
    public ResponseEntity<ModeloInspecao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modeloInspecaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar modelo de inspeção (somente em rascunho)")
    public ResponseEntity<ModeloInspecao> atualizar(@PathVariable Long id, @RequestBody @Valid ModeloInspecaoRequestDTO dto) {
        return ResponseEntity.ok(modeloInspecaoService.atualizarModelo(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir modelo de inspeção (somente em rascunho)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        modeloInspecaoService.excluirModelo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publicar")
    @Operation(summary = "Publicar versão imutável do modelo", description = "Valida a estrutura do checklist e torna o modelo imutável para uso em campo")
    public ResponseEntity<ModeloInspecao> publicarModelo(@PathVariable Long id) {
        return ResponseEntity.ok(modeloInspecaoService.publicarModelo(id));
    }

    @PostMapping("/{id}/nova-versao")
    @Operation(summary = "Criar nova versão em rascunho a partir de um modelo existente", description = "Preserva a versão anterior inalterada e incrementa o número da versão")
    public ResponseEntity<ModeloInspecao> criarNovaVersao(@PathVariable Long id) {
        ModeloInspecao novaVersao = modeloInspecaoService.criarNovaVersao(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVersao);
    }

    // Endpoints de Seções (PBI-019)
    @PostMapping("/{id}/secoes")
    @Operation(summary = "Adicionar seção ao modelo de inspeção")
    public ResponseEntity<SecaoInspecao> adicionarSecao(@PathVariable Long id, @RequestBody @Valid SecaoInspecaoRequestDTO dto) {
        SecaoInspecao secao = modeloInspecaoService.adicionarSecao(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(secao);
    }

    @PutMapping("/secoes/{secaoId}")
    @Operation(summary = "Editar título ou ordem de uma seção")
    public ResponseEntity<SecaoInspecao> atualizarSecao(@PathVariable Long secaoId, @RequestBody @Valid SecaoInspecaoRequestDTO dto) {
        return ResponseEntity.ok(modeloInspecaoService.atualizarSecao(secaoId, dto));
    }

    @DeleteMapping("/secoes/{secaoId}")
    @Operation(summary = "Excluir uma seção de um modelo em rascunho")
    public ResponseEntity<Void> excluirSecao(@PathVariable Long secaoId) {
        modeloInspecaoService.excluirSecao(secaoId);
        return ResponseEntity.noContent().build();
    }

    // Endpoints de Itens (PBI-020 & PBI-021)
    @PostMapping("/secoes/{secaoId}/itens")
    @Operation(summary = "Adicionar item com tipo de resposta e regras de evidência à seção")
    public ResponseEntity<ItemInspecao> adicionarItem(@PathVariable Long secaoId, @RequestBody @Valid ItemInspecaoRequestDTO dto) {
        ItemInspecao item = modeloInspecaoService.adicionarItem(secaoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/itens/{itemId}")
    @Operation(summary = "Editar configurações de um item")
    public ResponseEntity<ItemInspecao> atualizarItem(@PathVariable Long itemId, @RequestBody @Valid ItemInspecaoRequestDTO dto) {
        return ResponseEntity.ok(modeloInspecaoService.atualizarItem(itemId, dto));
    }

    @DeleteMapping("/itens/{itemId}")
    @Operation(summary = "Excluir um item de verificação")
    public ResponseEntity<Void> excluirItem(@PathVariable Long itemId) {
        modeloInspecaoService.excluirItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
