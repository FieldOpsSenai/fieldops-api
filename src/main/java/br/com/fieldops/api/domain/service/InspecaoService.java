package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Equipamento;
import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.StatusInspecao;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.ModeloInspecaoRepository;
import br.com.fieldops.api.domain.repository.InspecaoSnapshotItemRepository;
import br.com.fieldops.api.domain.repository.RespostaInspecaoRepository;
import br.com.fieldops.api.domain.repository.FotoInspecaoRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.presentation.dto.InspecaoCancelamentoDTO;
import br.com.fieldops.api.presentation.dto.InspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.InspecaoResponseDTO;
import br.com.fieldops.api.presentation.dto.InspecaoRevisaoDTO;
import br.com.fieldops.api.presentation.dto.InspecaoStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class InspecaoService {

    @Autowired
    private InspecaoRepository inspecaoRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModeloInspecaoRepository modeloInspecaoRepository;

    @Autowired
    private InspecaoSnapshotService snapshotService;

    @Autowired
    private InspecaoSnapshotItemRepository snapshotItemRepository;

    @Autowired
    private RespostaInspecaoRepository respostaInspecaoRepository;

    @Autowired
    private FotoInspecaoRepository fotoInspecaoRepository;

    @Transactional
    public InspecaoResponseDTO criar(InspecaoRequestDTO dto) {
        exigirPlanejador();
        Equipamento equipamento = equipamentoRepository.findByIdAndAtivoTrue(dto.getEquipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado com o ID: " + dto.getEquipamentoId()));

        ModeloInspecao modelo = modeloInspecaoRepository.findById(dto.getModeloId())
                .orElseThrow(() -> new IllegalArgumentException("Modelo de inspeção não encontrado com o ID: " + dto.getModeloId()));
        if (!"PUBLICADO".equalsIgnoreCase(modelo.getStatus())) {
            throw new IllegalArgumentException("A inspeção só pode utilizar um modelo publicado.");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + dto.getUsuarioId()));

        if (!Perfil.TECNICO.equals(usuario.getPerfil()) || !Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new IllegalArgumentException("A inspeção só pode ser atribuída a um técnico ativo.");
        }

        validarCadeiaCadastro(dto, equipamento);
        if (dto.getDataLimite() != null && dto.getDataLimite().isBefore(dto.getDataAgendada())) {
            throw new IllegalArgumentException("O prazo limite não pode ser anterior à data agendada.");
        }

        Inspecao inspecao = new Inspecao();
        inspecao.setDescricao(dto.getDescricao());
        inspecao.setDataAgendada(dto.getDataAgendada());
        inspecao.setObservacoes(dto.getObservacoes());
        inspecao.setDataLimite(dto.getDataLimite());
        inspecao.setPrioridade(dto.getPrioridade() == null ? br.com.fieldops.api.domain.enums.PrioridadeInspecao.MEDIA : dto.getPrioridade());
        inspecao.setInstrucoes(dto.getInstrucoes());
        inspecao.setModeloInspecao(modelo);
        inspecao.setEquipamento(equipamento);
        inspecao.setUsuario(usuario);
        inspecao.setStatus(StatusInspecao.AGENDADA);

        Inspecao salva = inspecaoRepository.save(inspecao);
        snapshotService.gerarSnapshot(salva, modelo);
        return new InspecaoResponseDTO(salva);
    }

    public List<InspecaoResponseDTO> listarTodas() {
        exigirPlanejador();
        return inspecaoRepository.findAll().stream()
                .map(InspecaoResponseDTO::new)
                .toList();
    }

    public List<InspecaoResponseDTO> listarPorUsuario(Long usuarioId) {
        exigirPlanejador();
        return inspecaoRepository.findByUsuarioId(usuarioId).stream()
                .map(InspecaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InspecaoResponseDTO> listarMinhasInspecoes() {
        Usuario usuario = usuarioAutenticado();
        if (!Perfil.TECNICO.equals(usuario.getPerfil())) {
            throw new IllegalStateException("A consulta de inspeções atribuídas é exclusiva para técnicos.");
        }
        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new org.springframework.security.access.AccessDeniedException("Técnico inativo não pode consultar inspeções.");
        }
        return inspecaoRepository.findByUsuarioId(usuario.getId()).stream()
                .map(InspecaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public InspecaoResponseDTO buscarMinhaInspecao(Long id) {
        Usuario usuario = usuarioAutenticado();
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + id));
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || !Perfil.TECNICO.equals(usuario.getPerfil()) || !usuario.getId().equals(inspecao.getUsuario().getId())) {
            throw new org.springframework.security.access.AccessDeniedException("A inspeção não está atribuída ao técnico autenticado.");
        }
        return new InspecaoResponseDTO(inspecao);
    }

    @Transactional(readOnly = true)
    public br.com.fieldops.api.presentation.dto.ConfirmacaoEquipamentoResponseDTO confirmarEquipamento(Long inspecaoId, String codigoQr) {
        Inspecao inspecao = inspecaoRepository.findById(inspecaoId)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + inspecaoId));
        Usuario usuario = usuarioAutenticado();
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || !Perfil.TECNICO.equals(usuario.getPerfil()) || !usuario.getId().equals(inspecao.getUsuario().getId())) {
            throw new org.springframework.security.access.AccessDeniedException("A inspeção não está atribuída ao técnico autenticado.");
        }
        Equipamento equipamento = equipamentoRepository.findByQrCodeAndAtivoTrue(codigoQr.trim())
                .orElseThrow(() -> new IllegalArgumentException("Nenhum equipamento ativo foi encontrado para o QR Code informado."));
        if (!equipamento.getId().equals(inspecao.getEquipamento().getId())) {
            throw new IllegalArgumentException("O QR Code não corresponde ao equipamento vinculado à inspeção.");
        }
        return new br.com.fieldops.api.presentation.dto.ConfirmacaoEquipamentoResponseDTO(inspecaoId, equipamento.getId(), true, "Equipamento confirmado com sucesso.");
    }

    @Transactional(readOnly = true)
    public Page<InspecaoResponseDTO> pesquisar(String termo, StatusInspecao status, Long usuarioId, Long clienteId,
                                                br.com.fieldops.api.domain.enums.PrioridadeInspecao prioridade, Pageable pageable) {
        exigirPlanejador();
        return inspecaoRepository.pesquisar(normalizarFiltro(termo), status, usuarioId, clienteId, prioridade, pageable)
                .map(InspecaoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<br.com.fieldops.api.presentation.dto.UsuarioResponseDTO> listarTecnicosAtivos() {
        exigirPlanejador();
        return usuarioRepository.findByPerfilAndAtivoTrue(Perfil.TECNICO).stream()
                .map(br.com.fieldops.api.presentation.dto.UsuarioResponseDTO::new)
                .toList();
    }

    @Transactional
    public InspecaoResponseDTO atualizarStatus(Long id, InspecaoStatusDTO dto) {
        exigirPlanejador();
        if (StatusInspecao.CONCLUIDA.equals(dto.getStatus())) {
            throw new IllegalArgumentException("Use o endpoint de conclusão para validar respostas e evidências antes de concluir a inspeção.");
        }
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + id));

        inspecao.setStatus(dto.getStatus());

        if (dto.getObservacoes() != null && !dto.getObservacoes().isBlank()) {
            inspecao.setObservacoes(dto.getObservacoes());
        }

        if (StatusInspecao.CONCLUIDA.equals(dto.getStatus())) {
            inspecao.setDataRealizacao(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        }

        return new InspecaoResponseDTO(inspecaoRepository.save(inspecao));
    }

    @Transactional
    public InspecaoResponseDTO concluir(Long id) {
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + id));
        Usuario usuario = usuarioAutenticado();
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || !Perfil.TECNICO.equals(usuario.getPerfil()) || !usuario.getId().equals(inspecao.getUsuario().getId())) {
            throw new org.springframework.security.access.AccessDeniedException("A inspeção não está atribuída ao técnico autenticado.");
        }
        if (inspecao.getStatus() != StatusInspecao.EM_ANDAMENTO && inspecao.getStatus() != StatusInspecao.PENDENTE && inspecao.getStatus() != StatusInspecao.AGENDADA) {
            throw new IllegalStateException("A inspeção não está em um estado que permita conclusão.");
        }

        Set<Long> respondidos = respostaInspecaoRepository.findByInspecaoId(id).stream()
                .filter(r -> r.getValor() != null && !r.getValor().isBlank())
                .map(br.com.fieldops.api.domain.entity.RespostaInspecao::getItemModeloId)
                .collect(Collectors.toSet());
        Set<Long> itensComFoto = fotoInspecaoRepository.findByInspecaoId(id).stream()
                .filter(f -> f.getSnapshotItem() != null)
                .map(f -> f.getSnapshotItem().getId())
                .collect(Collectors.toSet());
        List<String> pendencias = new ArrayList<>();
        snapshotItemRepository.findByInspecaoIdOrderByOrdemAsc(id).forEach(item -> {
            if (Boolean.TRUE.equals(item.getObrigatorio()) && !respondidos.contains(item.getId())) {
                pendencias.add("Item obrigatório sem resposta: " + item.getItemDescricao());
            }
            if (Boolean.TRUE.equals(item.getEvidenciaObrigatoria()) && !itensComFoto.contains(item.getId())) {
                pendencias.add("Item sem evidência fotográfica: " + item.getItemDescricao());
            }
        });
        if (!pendencias.isEmpty()) {
            throw new IllegalStateException("Não é possível concluir a inspeção. Pendências: " + String.join("; ", pendencias));
        }
        inspecao.setStatus(StatusInspecao.CONCLUIDA);
        inspecao.setDataRealizacao(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        return new InspecaoResponseDTO(inspecaoRepository.save(inspecao));
    }

    @Transactional
    public InspecaoResponseDTO cancelarInspecao(Long id, InspecaoCancelamentoDTO dto) {
        exigirPlanejador();
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + id));

        if (StatusInspecao.CONCLUIDA.equals(inspecao.getStatus()) || StatusInspecao.CANCELADA.equals(inspecao.getStatus()) || StatusInspecao.EM_ANDAMENTO.equals(inspecao.getStatus())) {
            throw new IllegalStateException("Apenas inspeções agendadas ou pendentes podem ser canceladas.");
        }

        inspecao.setStatus(StatusInspecao.CANCELADA);
        inspecao.setMotivoCancelamento(dto.getMotivo().trim());
        inspecao.setDataCancelamento(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        inspecao.setCanceladoPorUsuarioId(usuarioAutenticado().getId());

        return new InspecaoResponseDTO(inspecaoRepository.save(inspecao));
    }

    @Transactional
    public InspecaoResponseDTO atualizarPlanejamento(Long id, br.com.fieldops.api.presentation.dto.InspecaoPlanejamentoUpdateDTO dto) {
        exigirPlanejador();
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com o ID: " + id));
        if (inspecao.getStatus() != StatusInspecao.AGENDADA && inspecao.getStatus() != StatusInspecao.PENDENTE) {
            throw new IllegalStateException("O planejamento só pode ser alterado em inspeções agendadas ou pendentes.");
        }
        if (dto.getDataLimite() != null && dto.getDataLimite().isBefore(inspecao.getDataAgendada())) {
            throw new IllegalArgumentException("O prazo limite não pode ser anterior à data agendada.");
        }
        inspecao.setPrioridade(dto.getPrioridade());
        inspecao.setDataLimite(dto.getDataLimite());
        inspecao.setInstrucoes(dto.getInstrucoes());
        return new InspecaoResponseDTO(inspecaoRepository.save(inspecao));
    }

    @Transactional
    public InspecaoResponseDTO revisarInspecao(Long id, InspecaoRevisaoDTO dto) {
        exigirPlanejador();
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com ID: " + id));

        Usuario supervisor = usuarioRepository.findById(dto.getSupervisorId())
                .orElseThrow(() -> new IllegalArgumentException("Supervisor não encontrado com ID: " + dto.getSupervisorId()));

        if (!"SUPERVISOR".equals(supervisor.getPerfil())) {
            throw new IllegalArgumentException("O usuário informado não possui perfil de Supervisor.");
        }

        inspecao.setSupervisorId(supervisor.getId());
        inspecao.setDataRevisao(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        if (Boolean.TRUE.equals(dto.getAprovado())) {
            inspecao.setStatus(StatusInspecao.CONCLUIDA);
            inspecao.setMotivoReprovacao(null);
        } else {
            if (dto.getMotivoReprovacao() == null || dto.getMotivoReprovacao().isBlank()) {
                throw new IllegalArgumentException("O motivo da reprovação é obrigatório quando a inspeção é rejeitada.");
            }
            inspecao.setStatus(StatusInspecao.EM_ANDAMENTO);
            inspecao.setMotivoReprovacao(dto.getMotivoReprovacao());
        }

        Inspecao inspecaoSalva = inspecaoRepository.save(inspecao);
        return new InspecaoResponseDTO(inspecaoSalva);
    }

    private void validarCadeiaCadastro(InspecaoRequestDTO dto, Equipamento equipamento) {
        if (dto.getLocalId() != null && !dto.getLocalId().equals(equipamento.getLocal().getId())) {
            throw new IllegalArgumentException("O equipamento não pertence ao local informado.");
        }
        if (dto.getClienteId() != null && !dto.getClienteId().equals(equipamento.getLocal().getCliente().getId())) {
            throw new IllegalArgumentException("O equipamento não pertence ao cliente informado.");
        }
    }

    private String normalizarFiltro(String termo) {
        return termo == null || termo.isBlank() ? null : termo.trim();
    }

    private Usuario usuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));
    }

    private void exigirPlanejador() {
        Usuario usuario = usuarioAutenticado();
        if (!Boolean.TRUE.equals(usuario.getAtivo()) ||
                (usuario.getPerfil() != Perfil.SUPERVISOR && usuario.getPerfil() != Perfil.ADMINISTRADOR)) {
            throw new org.springframework.security.access.AccessDeniedException("Esta operação exige um supervisor ou administrador ativo.");
        }
    }
}
