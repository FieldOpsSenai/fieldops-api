package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.RespostaInspecaoService;
import br.com.fieldops.api.presentation.dto.RespostaInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.RespostaInspecaoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inspecoes/{inspecaoId}/respostas")
public class RespostaInspecaoController {
    private final RespostaInspecaoService service;
    public RespostaInspecaoController(RespostaInspecaoService service) { this.service = service; }

    @PutMapping
    public ResponseEntity<RespostaInspecaoResponseDTO> salvar(@PathVariable Long inspecaoId,
                                                                @RequestBody @Valid RespostaInspecaoRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(inspecaoId, dto));
    }

    @GetMapping
    public ResponseEntity<List<RespostaInspecaoResponseDTO>> listar(@PathVariable Long inspecaoId) {
        return ResponseEntity.ok(service.listar(inspecaoId));
    }
}
