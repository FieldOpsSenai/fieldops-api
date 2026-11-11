package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.InspecaoSnapshotItem;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.SecaoInspecao;
import br.com.fieldops.api.domain.entity.ItemInspecao;
import br.com.fieldops.api.domain.repository.InspecaoSnapshotItemRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.presentation.dto.InspecaoSnapshotItemResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class InspecaoSnapshotService {

    private final InspecaoSnapshotItemRepository snapshotItemRepository;
    private final InspecaoRepository inspecaoRepository;
    private final UsuarioRepository usuarioRepository;

    public InspecaoSnapshotService(InspecaoSnapshotItemRepository snapshotItemRepository,
                                   InspecaoRepository inspecaoRepository, UsuarioRepository usuarioRepository) {
        this.snapshotItemRepository = snapshotItemRepository;
        this.inspecaoRepository = inspecaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * PBI-024: Preserva um snapshot completo e imutável de todas as seções e itens
     * do modelo utilizado no momento do agendamento da inspeção.
     */
    @Transactional
    public List<InspecaoSnapshotItem> gerarSnapshot(Inspecao inspecao, ModeloInspecao modelo) {
        List<InspecaoSnapshotItem> itensSnapshot = new ArrayList<>();

        if (modelo.getSecoes() == null || modelo.getSecoes().isEmpty()) {
            return itensSnapshot;
        }

        List<SecaoInspecao> secoesOrdenadas = modelo.getSecoes().stream()
                .sorted(Comparator.comparing(SecaoInspecao::getOrdem, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        for (SecaoInspecao secao : secoesOrdenadas) {
            if (secao.getItens() != null) {
                List<ItemInspecao> itensOrdenados = secao.getItens().stream()
                        .sorted(Comparator.comparing(ItemInspecao::getOrdem, Comparator.nullsLast(Comparator.naturalOrder())))
                        .toList();

                for (ItemInspecao item : itensOrdenados) {
                    InspecaoSnapshotItem snapshot = new InspecaoSnapshotItem();
                    snapshot.setInspecao(inspecao);
                    snapshot.setSecaoTitulo(secao.getTitulo());
                    snapshot.setItemOriginalId(item.getId());
                    snapshot.setItemDescricao(item.getDescricao());
                    snapshot.setTipoResposta(item.getTipoResposta());
                    snapshot.setOrdem(item.getOrdem());
                    snapshot.setObrigatorio(Boolean.TRUE.equals(item.getObrigatorio()));
                    snapshot.setCritico(Boolean.TRUE.equals(item.getCritico()));
                    snapshot.setEvidenciaObrigatoria(Boolean.TRUE.equals(item.getEvidenciaObrigatoria()));

                    itensSnapshot.add(snapshot);
                }
            }
        }

        return snapshotItemRepository.saveAll(itensSnapshot);
    }

    /**
     * PBI-024: Recupera o snapshot imutável associado a uma inspeção específica.
     */
    @Transactional(readOnly = true)
    public List<InspecaoSnapshotItemResponseDTO> recuperarSnapshot(Long inspecaoId) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        exigirAcesso(inspecao);
        return snapshotItemRepository.findByInspecaoIdOrderByOrdemAsc(inspecaoId).stream()
                .map(InspecaoSnapshotItemResponseDTO::new)
                .toList();
    }

    private void exigirAcesso(Inspecao inspecao) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Usuário autenticado não encontrado."));
        boolean gestor = usuario.getPerfil() == Perfil.SUPERVISOR || usuario.getPerfil() == Perfil.ADMINISTRADOR;
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || (!gestor && !usuario.getId().equals(inspecao.getUsuario().getId()))) {
            throw new AccessDeniedException("Sem acesso à inspeção informada.");
        }
    }
}

