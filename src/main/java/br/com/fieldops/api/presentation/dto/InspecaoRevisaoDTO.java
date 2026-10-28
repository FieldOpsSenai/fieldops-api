package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotNull;

public class InspecaoRevisaoDTO {

    @NotNull(message = "O ID do supervisor é obrigatório")
    private Long supervisorId;

    @NotNull(message = "O status de aprovação é obrigatório")
    private Boolean aprovado; // true para aprovada, false para reprovada

    private String motivoReprovacao; // Obrigatório se aprovado = false

    // Getters e Setters
    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }

    public Boolean getAprovado() { return aprovado; }
    public void setAprovado(Boolean aprovado) { this.aprovado = aprovado; }

    public String getMotivoReprovacao() { return motivoReprovacao; }
    public void setMotivoReprovacao(String motivoReprovacao) { this.motivoReprovacao = motivoReprovacao; }
}