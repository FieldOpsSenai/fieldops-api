package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.entity.Cliente;
import br.com.fieldops.api.domain.repository.ClienteRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.presentation.dto.ClienteRequestDTO;
import br.com.fieldops.api.presentation.dto.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
@SecurityRequirement(name = "bearer-key")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LocalRepository localRepository;

    @PostMapping
    @Operation(summary = "Cadastrar novo cliente")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ClienteRequestDTO dto) {
        if (clienteRepository.existsByCnpj(dto.getCnpj())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe um cliente cadastrado com este CNPJ");
        }

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe um cliente cadastrado com este e-mail");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCnpj(dto.getCnpj());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setAtivo(true);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClienteResponseDTO(clienteSalvo));
    }

    @GetMapping
    @Operation(summary = "Listar e pesquisar clientes com paginação")
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<Cliente> clientes;
        if (termo != null && !termo.isBlank()) {
            clientes = clienteRepository.pesquisarAtivos(termo.trim(), pageable);
        } else {
            clientes = clienteRepository.findByAtivoTrue(pageable);
        }

        return ResponseEntity.ok(clientes.map(ClienteResponseDTO::new));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        return ResponseEntity.ok(new ClienteResponseDTO(cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um cliente")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        // Validação de unicidade de CNPJ caso alterado
        if (!cliente.getCnpj().equals(dto.getCnpj()) && clienteRepository.existsByCnpj(dto.getCnpj())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe um cliente cadastrado com este CNPJ");
        }

        // Validação de unicidade de e-mail caso alterado
        if (!cliente.getEmail().equalsIgnoreCase(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe um cliente cadastrado com este e-mail");
        }

        cliente.setNome(dto.getNome());
        cliente.setCnpj(dto.getCnpj());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return ResponseEntity.ok(new ClienteResponseDTO(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar cliente respeitando dependências")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        // Respeitar dependências existentes (RN-013 / Critério de aceite)
        if (localRepository.existsByClienteIdAndAtivoTrue(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não é possível inativar o cliente pois existem locais ativos vinculados a ele.");
        }

        cliente.setAtivo(false);
        clienteRepository.save(cliente);

        return ResponseEntity.noContent().build();
    }
}
