package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.service.FotoInspecaoService;
import br.com.fieldops.api.presentation.dto.FotoInspecaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inspecoes/{inspecaoId}/fotos")
public class FotoInspecaoController {

    @Autowired
    private FotoInspecaoService fotoInspecaoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoInspecaoResponseDTO> uploadFoto(
            @PathVariable Long inspecaoId,
            @RequestParam(required = false) Long snapshotItemId,
            @RequestParam("arquivo") MultipartFile arquivo) {

        FotoInspecaoResponseDTO response = fotoInspecaoService.salvarFoto(inspecaoId, snapshotItemId, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FotoInspecaoResponseDTO>> listarFotos(@PathVariable Long inspecaoId) {
        return ResponseEntity.ok(fotoInspecaoService.listarPorInspecao(inspecaoId));
    }

    @GetMapping("/itens/{snapshotItemId}")
    public ResponseEntity<List<FotoInspecaoResponseDTO>> listarFotosDoItem(
            @PathVariable Long inspecaoId, @PathVariable Long snapshotItemId) {
        return ResponseEntity.ok(fotoInspecaoService.listarPorItem(inspecaoId, snapshotItemId));
    }

    @GetMapping("/{fotoId}/arquivo")
    public ResponseEntity<Resource> visualizarFoto(
            @PathVariable Long inspecaoId, @PathVariable Long fotoId) {
        FotoInspecaoService.FotoArquivoDTO arquivo = fotoInspecaoService.carregarArquivo(inspecaoId, fotoId);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (arquivo.foto().getTipoConteudo() != null && !arquivo.foto().getTipoConteudo().isBlank()) {
            mediaType = MediaType.parseMediaType(arquivo.foto().getTipoConteudo());
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.foto().getNomeArquivo() + "\"")
                .body(arquivo.resource());
    }
}
