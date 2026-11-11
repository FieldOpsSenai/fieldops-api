package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RespostaInspecaoRequestDTO {
    @NotNull(message = "O item do snapshot é obrigatório")
    private Long snapshotItemId;
    private String valor;
    private String observacao;
}
