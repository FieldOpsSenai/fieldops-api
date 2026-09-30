package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    @Schema(description = "Identificador único do cliente no sistema", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente ou razão social", example = "Indústrias Metalúrgicas Silva LTDA")
    private String nome;

    @Schema(description = "CNPJ do cliente", example = "12.345.678/0001-90")
    private String cnpj;

    @Schema(description = "Telefone principal de contato", example = "(15) 99876-5432")
    private String telefone;

    @Schema(description = "E-mail corporativo de contato", example = "contato@metalurgicasilva.com.br")
    private String email;

    @Schema(description = "Status de cadastro do cliente", example = "true")
    private Boolean ativo;

    // Construtor auxiliar para converter a Entidade direto para o DTO
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cnpj = cliente.getCnpj();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.ativo = cliente.getAtivo();
    }
}