package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class InspecaoCancelamentoDTO {

    @NotBlank(message = "O motivo do cancelamento é obrigatório.")
    @Schema(description = "Motivo pelo qual a inspeção está sendo cancelada", example = "Equipamento em manutenção corretiva não programada.")
    private String motivo;

    public InspecaoCancelamentoDTO() {}

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}