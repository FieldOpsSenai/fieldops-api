package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.FotoInspecaoService;
import br.com.fieldops.api.presentation.dto.FotoInspecaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fotos-inspecao")
public class FotoInspecaoController {

    @Autowired
    private FotoInspecaoService fotoInspecaoService;

    @PostMapping("/inspecao/{inspecaoId}")
    public ResponseEntity<FotoInspecaoResponseDTO> salvarFoto(
            @PathVariable UUID inspecaoId,
            @RequestParam("file") MultipartFile file) {
        FotoInspecaoResponseDTO response = fotoInspecaoService.salvarFoto(inspecaoId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inspecao/{inspecaoId}")
    public ResponseEntity<List<FotoInspecaoResponseDTO>> listarPorInspecao(@PathVariable UUID inspecaoId) {
        List<FotoInspecaoResponseDTO> fotos = fotoInspecaoService.listarPorInspecao(inspecaoId);
        return ResponseEntity.ok(fotos);
    }
}