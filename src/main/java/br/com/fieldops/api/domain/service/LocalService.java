package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Cliente;
import br.com.fieldops.api.domain.entity.Local;
import br.com.fieldops.api.domain.repository.ClienteRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.presentation.dto.LocalRequestDTO;
import br.com.fieldops.api.presentation.dto.LocalResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final ClienteRepository clienteRepository;

    public LocalService(LocalRepository localRepository, ClienteRepository clienteRepository) {
        this.localRepository = localRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public LocalResponseDTO criar(LocalRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Local local = new Local();
        local.setNome(dto.getNome());
        local.setEndereco(dto.getEndereco());
        local.setCliente(cliente);

        Local salvo = localRepository.save(local);
        return new LocalResponseDTO(salvo);
    }

    public List<LocalResponseDTO> listarTodos() {
        return localRepository.findAll().stream()
                .map(LocalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<LocalResponseDTO> listarPorCliente(Long clienteId) {
        return localRepository.findAll().stream()
                .filter(l -> l.getCliente() != null && l.getCliente().getId().equals(clienteId))
                .map(LocalResponseDTO::new)
                .collect(Collectors.toList());
    }
}