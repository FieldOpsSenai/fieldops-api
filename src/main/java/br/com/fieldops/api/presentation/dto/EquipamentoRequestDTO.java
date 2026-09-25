package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipamentoRequestDTO {

    private String codigoQr;

    @NotBlank(message = "O número de série é obrigatório")
    private String numeroSerie;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "O status é obrigatório")
    private String status;

    @NotNull(message = "O ID do local é obrigatório")
    private Long localId;
}