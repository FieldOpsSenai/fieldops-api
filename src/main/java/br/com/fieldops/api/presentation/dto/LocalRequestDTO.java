package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocalRequestDTO {

    @Schema(description = "Nome do local ou filial de inspeção", example = "Galpão Industrial - Setor A")
    @NotBlank(message = "O nome do local é obrigatório")
    private String nome;

    @Schema(description = "Endereço completo do local", example = "Av. Engenheiro Carlos Reinaldo Mendes, 2015 - Sorocaba/SP")
    private String endereco;

    @Schema(description = "ID do cliente ao qual o local pertence", example = "1")
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;
}