package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.enums.PrioridadeInspecao;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspecaoPlanejamentoUpdateDTO {
    @NotNull(message = "A prioridade é obrigatória")
    private PrioridadeInspecao prioridade;

    @FutureOrPresent(message = "O prazo limite deve ser no presente ou futuro")
    private LocalDateTime dataLimite;

    private String instrucoes;
}
