package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.StatusInspecao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class InspecaoStatusDTO {

    @Schema(description = "Novo status da inspeção", example = "CONCLUIDA")
    @NotNull(message = "O novo status é obrigatório")
    private StatusInspecao status;

    @Schema(description = "Observações ou justificativa da alteração de status", example = "Equipamento testado e aprovado com sucesso.")
    private String observacoes;

    public InspecaoStatusDTO() {
    }

    public InspecaoStatusDTO(StatusInspecao status, String observacoes) {
        this.status = status;
        this.observacoes = observacoes;
    }

    public StatusInspecao getStatus() {
        return status;
    }

    public void setStatus(StatusInspecao status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}