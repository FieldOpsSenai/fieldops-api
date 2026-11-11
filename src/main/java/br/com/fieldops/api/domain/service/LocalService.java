package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Cliente;
import br.com.fieldops.api.domain.entity.Local;
import br.com.fieldops.api.domain.repository.ClienteRepository;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.presentation.dto.LocalRequestDTO;
import br.com.fieldops.api.presentation.dto.LocalResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Transactional
    public LocalResponseDTO criar(LocalRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));

        if (!Boolean.TRUE.equals(cliente.getAtivo())) {
            throw new IllegalArgumentException("Não é possível associar um local a um cliente inativo.");
        }

        Local local = new Local();
        local.setNome(dto.getNome());
        local.setEndereco(dto.getEndereco());
        local.setCliente(cliente);
        local.setAtivo(true);

        return new LocalResponseDTO(localRepository.save(local));
    }

    public List<LocalResponseDTO> listarTodos() {
        return localRepository.findByAtivoTrue().stream()
                .map(LocalResponseDTO::new)
                .toList();
    }

    public List<LocalResponseDTO> listarPorCliente(Long clienteId) {
        return localRepository.findByClienteIdAndAtivoTrue(clienteId).stream()
                .map(LocalResponseDTO::new)
                .toList();
    }

    public Page<LocalResponseDTO> listarPaginado(String termo, Long clienteId, Pageable pageable) {
        return localRepository.pesquisarAtivos(termo, clienteId, pageable)
                .map(LocalResponseDTO::new);
    }

    public LocalResponseDTO buscarPorId(Long id) {
        Local local = localRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + id));
        return new LocalResponseDTO(local);
    }

    @Transactional
    public LocalResponseDTO atualizar(Long id, LocalRequestDTO dto) {
        Local local = localRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + id));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));

        if (!Boolean.TRUE.equals(cliente.getAtivo())) {
            throw new IllegalArgumentException("Não é possível associar um local a um cliente inativo.");
        }

        local.setNome(dto.getNome());
        local.setEndereco(dto.getEndereco());
        local.setCliente(cliente);

        return new LocalResponseDTO(localRepository.save(local));
    }

    @Transactional
    public void inativar(Long id) {
        Local local = localRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + id));

        // Respeita dependências existentes (Card 14)
        if (equipamentoRepository.existsByLocalIdAndAtivoTrue(id)) {
            throw new IllegalStateException("Não é possível excluir o local pois existem equipamentos vinculados a ele.");
        }

        local.setAtivo(false);
        localRepository.save(local);
    }
}
