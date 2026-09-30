package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.StatusInspecao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class InspecaoResponseDTO {

    @Schema(description = "Identificador único da inspeção", example = "101")
    private Long id;

    @Schema(description = "Descrição detalhada da atividade de inspeção", example = "Inspeção preventiva do compressor de ar")
    private String descricao;

    @Schema(description = "Status atual da inspeção", example = "EM_ANDAMENTO")
    private StatusInspecao status;

    @Schema(description = "Data e hora agendadas para realização", example = "2026-10-05T09:00:00")
    private LocalDateTime dataAgendada;

    @Schema(description = "Data e hora em que a inspeção foi efetivamente realizada", example = "2026-10-05T09:45:00")
    private LocalDateTime dataRealizacao;

    @Schema(description = "Observações e parecer do técnico", example = "Necessário substituir filtro de ar na próxima manutenção.")
    private String observacoes;

    @Schema(description = "ID do equipamento inspecionado", example = "1")
    private Long equipamentoId;

    @Schema(description = "Nome do equipamento inspecionado", example = "Compressor de Ar Parafuso 20HP")
    private String equipamentoNome;

    @Schema(description = "ID do técnico responsável", example = "2")
    private Long usuarioId;

    @Schema(description = "Nome do técnico responsável", example = "Carlos Eduardo")
    private String usuarioNome;

    public InspecaoResponseDTO() {
    }

    public InspecaoResponseDTO(Inspecao inspecao) {
        this.id = inspecao.getId();
        this.descricao = inspecao.getDescricao();
        this.status = inspecao.getStatus();
        this.dataAgendada = inspecao.getDataAgendada();
        this.dataRealizacao = inspecao.getDataRealizacao();
        this.observacoes = inspecao.getObservacoes();
        
        if (inspecao.getEquipamento() != null) {
            this.equipamentoId = inspecao.getEquipamento().getId();
            this.equipamentoNome = inspecao.getEquipamento().getNome();
        }
        
        if (inspecao.getUsuario() != null) {
            this.usuarioId = inspecao.getUsuario().getId();
            this.usuarioNome = inspecao.getUsuario().getNome();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusInspecao getStatus() {
        return status;
    }

    public void setStatus(StatusInspecao status) {
        this.status = status;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public LocalDateTime getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDateTime dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
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

    public String getEquipamentoNome() {
        return equipamentoNome;
    }

    public void setEquipamentoNome(String equipamentoNome) {
        this.equipamentoNome = equipamentoNome;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }
}