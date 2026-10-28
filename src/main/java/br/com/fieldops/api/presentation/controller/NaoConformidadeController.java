package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.entity.NaoConformidade;
import br.com.fieldops.api.domain.service.NaoConformidadeService;
import br.com.fieldops.api.presentation.dto.NaoConformidadeRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nao-conformidades")
public class NaoConformidadeController {

    private final NaoConformidadeService service;

    public NaoConformidadeController(NaoConformidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NaoConformidade> registrar(@Valid @RequestBody NaoConformidadeRequestDTO dto) {
        NaoConformidade novaNc = service.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaNc);
    }

    @GetMapping("/inspecao/{inspecaoId}")
    public ResponseEntity<List<NaoConformidade>> listarPorInspecao(@PathVariable Long inspecaoId) {
        List<NaoConformidade> ncs = service.listarPorInspecao(inspecaoId);
        return ResponseEntity.ok(ncs);
    }
}