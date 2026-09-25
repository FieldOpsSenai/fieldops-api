package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Equipamento;
import br.com.fieldops.api.domain.entity.Local;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.presentation.dto.EquipamentoRequestDTO;
import br.com.fieldops.api.presentation.dto.EquipamentoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final LocalRepository localRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, LocalRepository localRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.localRepository = localRepository;
    }

    @Transactional
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        Local local = localRepository.findById(dto.getLocalId())
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));

        Equipamento equipamento = new Equipamento();
        equipamento.setCodigoQr(dto.getCodigoQr());
        equipamento.setNumeroSerie(dto.getNumeroSerie());
        equipamento.setCategoria(dto.getCategoria());
        equipamento.setStatus(dto.getStatus());
        equipamento.setLocal(local);

        Equipamento salvo = equipamentoRepository.save(equipamento);
        return new EquipamentoResponseDTO(salvo);
    }

    public List<EquipamentoResponseDTO> listarTodos() {
        return equipamentoRepository.findAll().stream()
                .map(EquipamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<EquipamentoResponseDTO> listarPorLocal(Long localId) {
        return equipamentoRepository.findAll().stream()
                .filter(e -> e.getLocal() != null && e.getLocal().getId().equals(localId))
                .map(EquipamentoResponseDTO::new)
                .collect(Collectors.toList());
    }
}