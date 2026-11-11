package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.FotoInspecao;
import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.repository.FotoInspecaoRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.InspecaoSnapshotItemRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.presentation.dto.FotoInspecaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class FotoInspecaoService {

    private final Path diretorioUpload = Paths.get("uploads/fotos");

    @Autowired
    private FotoInspecaoRepository fotoInspecaoRepository;

    @Autowired
    private InspecaoRepository inspecaoRepository;

    @Autowired
    private InspecaoSnapshotItemRepository snapshotItemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public FotoInspecaoResponseDTO salvarFoto(Long inspecaoId, Long snapshotItemId, MultipartFile arquivo) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirAcessoTecnico(inspecao);

        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de foto enviado está vazio.");
        }

        br.com.fieldops.api.domain.entity.InspecaoSnapshotItem snapshotItem = null;
        if (snapshotItemId != null) {
            snapshotItem = snapshotItemRepository.findByIdAndInspecaoId(snapshotItemId, inspecaoId)
                    .orElseThrow(() -> new IllegalArgumentException("O item informado não pertence à inspeção."));
        }

        try {
            if (!Files.exists(diretorioUpload)) {
                Files.createDirectories(diretorioUpload);
            }

            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            String nomeArquivoSalvo = UUID.randomUUID() + extensao;
            Path caminhoCompleto = diretorioUpload.resolve(nomeArquivoSalvo);

            Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

            FotoInspecao foto = new FotoInspecao();
            foto.setNomeArquivo(nomeArquivoSalvo);
            foto.setCaminhoArquivo(caminhoCompleto.toString());
            foto.setTipoConteudo(arquivo.getContentType());
            foto.setDataUpload(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
            foto.setInspecao(inspecao);
            foto.setSnapshotItem(snapshotItem);

            return new FotoInspecaoResponseDTO(fotoInspecaoRepository.save(foto));

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a foto no servidor: " + e.getMessage(), e);
        }
    }

    public List<FotoInspecaoResponseDTO> listarPorInspecao(Long inspecaoId) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirAcessoTecnico(inspecao);
        return fotoInspecaoRepository.findByInspecaoId(inspecaoId).stream()
                .map(FotoInspecaoResponseDTO::new)
                .toList();
    }

    public List<FotoInspecaoResponseDTO> listarPorItem(Long inspecaoId, Long snapshotItemId) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirAcessoTecnico(inspecao);
        snapshotItemRepository.findByIdAndInspecaoId(snapshotItemId, inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("O item informado não pertence à inspeção."));
        return fotoInspecaoRepository.findByInspecaoIdAndSnapshotItemId(inspecaoId, snapshotItemId).stream()
                .map(FotoInspecaoResponseDTO::new)
                .toList();
    }

    private void exigirAcessoTecnico(Inspecao inspecao) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Usuário autenticado não encontrado."));
        boolean gestor = usuario.getPerfil() == Perfil.SUPERVISOR || usuario.getPerfil() == Perfil.ADMINISTRADOR;
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || (!gestor && !usuario.getId().equals(inspecao.getUsuario().getId()))) {
            throw new AccessDeniedException("Sem acesso à inspeção informada.");
        }
    }
}
