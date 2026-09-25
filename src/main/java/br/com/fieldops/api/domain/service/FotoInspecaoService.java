package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.FotoInspecao;
import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.repository.FotoInspecaoRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.presentation.dto.FotoInspecaoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FotoInspecaoService {

    private final FotoInspecaoRepository fotoInspecaoRepository;
    private final InspecaoRepository inspecaoRepository;

    public FotoInspecaoService(FotoInspecaoRepository fotoInspecaoRepository,
                               InspecaoRepository inspecaoRepository) {
        this.fotoInspecaoRepository = fotoInspecaoRepository;
        this.inspecaoRepository = inspecaoRepository;
    }

    @Transactional
    public FotoInspecaoResponseDTO salvarFoto(UUID inspecaoId, MultipartFile arquivo) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com ID: " + inspecaoId));

        FotoInspecao foto = new FotoInspecao();
        foto.setInspecao(inspecao);
        foto.setCaminhoArquivo(arquivo.getOriginalFilename());

        FotoInspecao salva = fotoInspecaoRepository.save(foto);
        return new FotoInspecaoResponseDTO(salva);
    }

    public List<FotoInspecaoResponseDTO> listarPorInspecao(UUID inspecaoId) {
        return fotoInspecaoRepository.findByInspecaoId(inspecaoId).stream()
                .map(FotoInspecaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}