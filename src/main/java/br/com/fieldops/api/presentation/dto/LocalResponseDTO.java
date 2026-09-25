package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Local;
import lombok.Data;

@Data
public class LocalResponseDTO {

    private Long id;
    private String nome;
    private String endereco;
    private Long clienteId;
    private String clienteRazaoSocial;

    public LocalResponseDTO(Local local) {
        this.id = local.getId();
        this.nome = local.getNome();
        this.endereco = local.getEndereco();
        if (local.getCliente() != null) {
            this.clienteId = local.getCliente().getId();
            this.clienteRazaoSocial = local.getCliente().getRazaoSocial();
        }
    }
}