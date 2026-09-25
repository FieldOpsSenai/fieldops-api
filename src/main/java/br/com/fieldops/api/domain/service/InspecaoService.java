package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Equipamento;
import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.presentation.dto.InspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.InspecaoResponseDTO;
import br.com.fieldops.api.presentation.dto.InspecaoStatusDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InspecaoService {

    private final InspecaoRepository inspecaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public InspecaoService(InspecaoRepository inspecaoRepository,
                          EquipamentoRepository equipamentoRepository,
                          UsuarioRepository usuarioRepository) {
        this.inspecaoRepository = inspecaoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public InspecaoResponseDTO criar(InspecaoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
        
        Usuario tecnico = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        Inspecao inspecao = new Inspecao();
        inspecao.setEquipamento(equipamento);
        inspecao.setTecnico(tecnico);
        inspecao.setStatus("PENDENTE");
        inspecao.setDataAgendamento(dto.getDataAgendada());
        inspecao.setObservacoesRevisao(dto.getObservacoes());

        Inspecao salva = inspecaoRepository.save(inspecao);
        return new InspecaoResponseDTO(salva);
    }

    public List<InspecaoResponseDTO> listarTodas() {
        return inspecaoRepository.findAll().stream()
                .map(InspecaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<InspecaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return inspecaoRepository.findAll().stream()
                .filter(i -> (i.getTecnico() != null && i.getTecnico().getId().equals(usuarioId)) ||
                             (i.getSupervisor() != null && i.getSupervisor().getId().equals(usuarioId)))
                .map(InspecaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public InspecaoResponseDTO atualizarStatus(UUID id, InspecaoStatusDTO dto) {
        Inspecao inspecao = inspecaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com ID: " + id));

        String novoStatus = dto.getStatus() != null ? dto.getStatus().toString() : "PENDENTE";
        
        inspecao.setStatus(novoStatus);
        inspecao.setObservacoesRevisao(dto.getObservacoes());

        if ("CONCLUIDA".equalsIgnoreCase(novoStatus)) {
            inspecao.setDataConclusao(LocalDateTime.now());
        } else if ("EM_ANDAMENTO".equalsIgnoreCase(novoStatus) && inspecao.getDataInicio() == null) {
            inspecao.setDataInicio(LocalDateTime.now());
        }

        Inspecao salva = inspecaoRepository.save(inspecao);
        return new InspecaoResponseDTO(salva);
    }
}