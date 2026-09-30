package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class InspecaoRequestDTO {

    @Schema(description = "Descrição detalhada da atividade a ser realizada", example = "Inspeção preventiva do compressor de ar")
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Data e hora agendadas para a realização da inspeção", example = "2026-10-05T09:00:00")
    @NotNull(message = "A data agendada é obrigatória")
    @FutureOrPresent(message = "A data agendada deve ser no presente ou futuro")
    private LocalDateTime dataAgendada;

    @Schema(description = "Observações iniciais ou instruções especiais para o técnico", example = "Verificar vazamento de óleo no cabeçote.")
    private String observacoes;

    @Schema(description = "ID do equipamento a ser inspecionado", example = "1")
    @NotNull(message = "O ID do equipamento é obrigatório")
    private Long equipamentoId;

    @Schema(description = "ID do técnico responsável pela inspeção", example = "2")
    @NotNull(message = "O ID do técnico (usuário) é obrigatório")
    private Long usuarioId;

    public InspecaoRequestDTO() {
    }

    public InspecaoRequestDTO(String descricao, LocalDateTime dataAgendada, String observacoes, Long equipamentoId, Long usuarioId) {
        this.descricao = descricao;
        this.dataAgendada = dataAgendada;
        this.observacoes = observacoes;
        this.equipamentoId = equipamentoId;
        this.usuarioId = usuarioId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public void setEquipamentoId(Long equipamentoId) {
        this.equipamentoId = equipamentoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}