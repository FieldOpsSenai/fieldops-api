package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Local;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocalResponseDTO {

    @Schema(description = "Identificador único do local", example = "10")
    private Long id;

    @Schema(description = "Nome do local ou setor", example = "Galpão Industrial - Setor A")
    private String nome;

    @Schema(description = "Endereço cadastrado", example = "Av. Engenheiro Carlos Reinaldo Mendes, 2015 - Sorocaba/SP")
    private String endereco;

    @Schema(description = "ID do cliente proprietário", example = "1")
    private Long clienteId;

    @Schema(description = "Nome ou Razão Social do cliente", example = "Indústrias Metalúrgicas Silva LTDA")
    private String clienteNome;

    @Schema(description = "Status de operação do local", example = "true")
    private Boolean ativo;

    public LocalResponseDTO(Local local) {
        this.id = local.getId();
        this.nome = local.getNome();
        this.endereco = local.getEndereco();
        this.clienteId = local.getCliente().getId();
        this.clienteNome = local.getCliente() != null ? local.getCliente().getNome() : null;
        this.ativo = local.getAtivo();
    }
}