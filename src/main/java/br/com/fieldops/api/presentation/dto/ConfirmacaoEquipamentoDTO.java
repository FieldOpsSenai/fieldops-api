package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmacaoEquipamentoDTO {
    @NotBlank(message = "O código QR é obrigatório")
    private String codigoQr;
}
