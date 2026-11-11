package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.RespostaInspecao;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.InspecaoSnapshotItemRepository;
import br.com.fieldops.api.domain.repository.RespostaInspecaoRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.presentation.dto.RespostaInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.RespostaInspecaoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class RespostaInspecaoService {
    private final RespostaInspecaoRepository respostaRepository;
    private final InspecaoRepository inspecaoRepository;
    private final InspecaoSnapshotItemRepository snapshotRepository;
    private final UsuarioRepository usuarioRepository;

    public RespostaInspecaoService(RespostaInspecaoRepository respostaRepository, InspecaoRepository inspecaoRepository,
                                   InspecaoSnapshotItemRepository snapshotRepository, UsuarioRepository usuarioRepository) {
        this.respostaRepository = respostaRepository;
        this.inspecaoRepository = inspecaoRepository;
        this.snapshotRepository = snapshotRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RespostaInspecaoResponseDTO salvar(Long inspecaoId, RespostaInspecaoRequestDTO dto) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirTecnicoAtribuido(inspecao);
        snapshotRepository.findByIdAndInspecaoId(dto.getSnapshotItemId(), inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("O item informado não pertence à inspeção."));
        RespostaInspecao resposta = respostaRepository.findByInspecaoIdAndItemModeloId(inspecaoId, dto.getSnapshotItemId())
                .orElseGet(RespostaInspecao::new);
        resposta.setInspecao(inspecao);
        resposta.setItemModeloId(dto.getSnapshotItemId());
        resposta.setValor(dto.getValor());
        resposta.setObservacao(dto.getObservacao());
        return new RespostaInspecaoResponseDTO(respostaRepository.save(resposta));
    }

    @Transactional(readOnly = true)
    public List<RespostaInspecaoResponseDTO> listar(Long inspecaoId) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirTecnicoAtribuido(inspecao);
        return respostaRepository.findByInspecaoId(inspecaoId).stream().map(RespostaInspecaoResponseDTO::new).toList();
    }

    private void exigirTecnicoAtribuido(Inspecao inspecao) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Usuário autenticado não encontrado."));
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || usuario.getPerfil() != Perfil.TECNICO ||
                !usuario.getId().equals(inspecao.getUsuario().getId())) {
            throw new AccessDeniedException("Apenas o técnico ativo atribuído pode responder esta inspeção.");
        }
    }
}
