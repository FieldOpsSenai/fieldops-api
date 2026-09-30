package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipamentoRequestDTO {

    @Schema(description = "Nome ou modelo do equipamento", example = "Compressor de Ar Parafuso 20HP")
    @NotBlank(message = "O nome do equipamento é obrigatório")
    private String nome;

    @Schema(description = "Número de série do fabricante", example = "CMP-2026-X987")
    @NotBlank(message = "O número de série é obrigatório")
    private String numeroSerie;

    @Schema(description = "Categoria ou tipo do equipamento", example = "Pneumático")
    private String tipo;

    @Schema(description = "ID do local/filial onde o equipamento está alocado", example = "10")
    @NotNull(message = "O ID do local é obrigatório")
    private Long localId;
}