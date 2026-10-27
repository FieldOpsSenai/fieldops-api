package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.SincronizacaoService;
import br.com.fieldops.api.presentation.dto.SincronizacaoDadosResponseDTO;
import br.com.fieldops.api.presentation.dto.SincronizacaoLoteRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sincronizacao")
@Tag(name = "Sincronização Offline", description = "Endpoints para carga de dados iniciais e envio de respostas do aplicativo móvel")
public class SincronizacaoController {

    private final SincronizacaoService sincronizacaoService;

    public SincronizacaoController(SincronizacaoService sincronizacaoService) {
        this.sincronizacaoService = sincronizacaoService;
    }

    @GetMapping("/dados/{usuarioId}")
    @Operation(summary = "Baixar dados para uso offline", description = "Retorna todos os modelos, locais, equipamentos e as inspeções pendentes do técnico para armazenamento local no app mobile")
    public ResponseEntity<SincronizacaoDadosResponseDTO> baixarDadosIniciais(@PathVariable Long usuarioId) {
        SincronizacaoDadosResponseDTO dados = sincronizacaoService.baixarDadosIniciais(usuarioId);
        return ResponseEntity.ok(dados);
    }

    @PostMapping("/sincronizar")
    @Operation(summary = "Sincronizar lote offline", description = "Recebe as respostas preenchidas em campo sem internet, garantindo idempotência e duplo controle de data/hora")
    public ResponseEntity<Void> sincronizarLote(
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody @Valid SincronizacaoLoteRequestDTO loteDto) {
        
        sincronizacaoService.processarLoteOffline(idempotencyKey, loteDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}