package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Equipamento;
import br.com.fieldops.api.domain.entity.Local;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.presentation.dto.EquipamentoRequestDTO;
import br.com.fieldops.api.presentation.dto.EquipamentoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private LocalRepository localRepository;

    @Transactional
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        if (equipamentoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new IllegalArgumentException("Já existe um equipamento cadastrado com o número de série: " + dto.getNumeroSerie());
        }

        Local local = localRepository.findByIdAndAtivoTrue(dto.getLocalId())
                .orElseThrow(() -> new EntityNotFoundException("Local ativo não encontrado com ID: " + dto.getLocalId()));

        Equipamento equipamento = new Equipamento();
        equipamento.setNome(dto.getNome());
        equipamento.setNumeroSerie(dto.getNumeroSerie());
        equipamento.setTipo(dto.getTipo());
        equipamento.setLocal(local);
        equipamento.setAtivo(true);

        // PBI-016: Geração e validação de QR Code único
        String qrCode = dto.getQrCode();
        if (qrCode != null && !qrCode.isBlank()) {
            if (equipamentoRepository.existsByQrCode(qrCode.trim())) {
                throw new IllegalArgumentException("Já existe um equipamento cadastrado com o código QR: " + qrCode);
            }
            equipamento.setQrCode(qrCode.trim());
        } else {
            // Gera automaticamente identificador exclusivo
            String novoQr;
            do {
                novoQr = "EQP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            } while (equipamentoRepository.existsByQrCode(novoQr));
            equipamento.setQrCode(novoQr);
        }

        return new EquipamentoResponseDTO(equipamentoRepository.save(equipamento));
    }

    public List<EquipamentoResponseDTO> listarTodos() {
        return equipamentoRepository.findByAtivoTrue().stream()
                .map(EquipamentoResponseDTO::new)
                .toList();
    }

    public List<EquipamentoResponseDTO> listarPorLocal(Long localId) {
        return equipamentoRepository.findByLocalIdAndAtivoTrue(localId).stream()
                .map(EquipamentoResponseDTO::new)
                .toList();
    }

    public Page<EquipamentoResponseDTO> listarPaginado(String termo, Long localId, Pageable pageable) {
        return equipamentoRepository.pesquisarAtivos(termo, localId, pageable)
                .map(EquipamentoResponseDTO::new);
    }

    public EquipamentoResponseDTO buscarPorId(Long id) {
        Equipamento equipamento = equipamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado com ID: " + id));
        return new EquipamentoResponseDTO(equipamento);
    }

    // PBI-016 & PBI-041: Consulta de equipamento por código QR
    public EquipamentoResponseDTO buscarPorQrCode(String qrCode) {
        Equipamento equipamento = equipamentoRepository.findByQrCodeAndAtivoTrue(qrCode)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum equipamento ativo encontrado para o QR Code: " + qrCode));
        return new EquipamentoResponseDTO(equipamento);
    }

    @Transactional
    public EquipamentoResponseDTO atualizar(Long id, EquipamentoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado com ID: " + id));

        if (!equipamento.getNumeroSerie().equals(dto.getNumeroSerie()) &&
                equipamentoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new IllegalArgumentException("Já existe um equipamento cadastrado com o número de série: " + dto.getNumeroSerie());
        }

        Local local = localRepository.findByIdAndAtivoTrue(dto.getLocalId())
                .orElseThrow(() -> new EntityNotFoundException("Local ativo não encontrado com ID: " + dto.getLocalId()));

        if (dto.getQrCode() != null && !dto.getQrCode().isBlank()) {
            if (!dto.getQrCode().trim().equals(equipamento.getQrCode()) &&
                    equipamentoRepository.existsByQrCode(dto.getQrCode().trim())) {
                throw new IllegalArgumentException("Já existe um equipamento com o código QR: " + dto.getQrCode());
            }
            equipamento.setQrCode(dto.getQrCode().trim());
        }

        equipamento.setNome(dto.getNome());
        equipamento.setNumeroSerie(dto.getNumeroSerie());
        equipamento.setTipo(dto.getTipo());
        equipamento.setLocal(local);

        return new EquipamentoResponseDTO(equipamentoRepository.save(equipamento));
    }

    @Transactional
    public void inativar(Long id) {
        Equipamento equipamento = equipamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado com ID: " + id));

        equipamento.setAtivo(false);
        equipamentoRepository.save(equipamento);
    }
}
