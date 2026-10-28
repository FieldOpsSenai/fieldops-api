package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.enums.CriticidadeNaoConformidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NaoConformidadeRequestDTO {

    @NotNull(message = "A criticidade é obrigatória")
    private CriticidadeNaoConformidade criticidade;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O ID da inspeção é obrigatório")
    private Long inspecaoId;

    private Long respostaId; // Opcional, caso a não conformidade esteja ligada a um item específico

    // Getters e Setters
    public CriticidadeNaoConformidade getCriticidade() { return criticidade; }
    public void setCriticidade(CriticidadeNaoConformidade criticidade) { this.criticidade = criticidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getInspecaoId() { return inspecaoId; }
    public void setInspecaoId(Long inspecaoId) { this.inspecaoId = inspecaoId; }

    public Long getRespostaId() { return respostaId; }
    public void setRespostaId(Long respostaId) { this.respostaId = respostaId; }
}