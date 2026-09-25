package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String contato;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.razaoSocial = cliente.getRazaoSocial();
        this.cnpj = cliente.getCnpj();
        this.contato = cliente.getContato();
    }
}