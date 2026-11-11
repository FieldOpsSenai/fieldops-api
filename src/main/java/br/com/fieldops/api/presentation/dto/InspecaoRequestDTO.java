package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.enums.PrioridadeInspecao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspecaoRequestDTO {

    @Schema(description = "Descrição detalhada da atividade a ser realizada", example = "Inspeção preventiva do compressor de ar")
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Data e hora agendadas para a realização da inspeção", example = "2026-10-05T09:00:00")
    @NotNull(message = "A data agendada é obrigatória")
    @FutureOrPresent(message = "A data agendada deve ser no presente ou futuro")
    private LocalDateTime dataAgendada;

    @Schema(description = "Prazo limite para a conclusão da inspeção", example = "2026-10-10T18:00:00")
    @FutureOrPresent(message = "O prazo limite deve ser no presente ou futuro")
    private LocalDateTime dataLimite;

    @Schema(description = "Nível de prioridade padronizado", example = "ALTA")
    private PrioridadeInspecao prioridade = PrioridadeInspecao.MEDIA;

    @Schema(description = "Instruções operacionais detalhadas para o técnico em campo", example = "Utilizar EPI completo e aferir pressão do manômetro.")
    private String instrucoes;

    @Schema(description = "Observações adicionais ou notas do planejamento", example = "Acesso pela portaria 2.")
    private String observacoes;

    @Schema(description = "ID do modelo de inspeção (checklist publicado)", example = "1")
    @NotNull(message = "O ID do modelo de inspeção publicado é obrigatório")
    private Long modeloId;

    @Schema(description = "ID do equipamento a ser inspecionado", example = "1")
    @NotNull(message = "O ID do equipamento é obrigatório")
    private Long equipamentoId;

    @Schema(description = "ID do local (opcional, para validação da seleção encadeada)", example = "5")
    private Long localId;

    @Schema(description = "ID do cliente (opcional, para validação da seleção encadeada)", example = "2")
    private Long clienteId;

    @Schema(description = "ID do técnico responsável pela inspeção (usuário ativo)", example = "2")
    @NotNull(message = "O ID do técnico (usuário) é obrigatório")
    private Long usuarioId;
}
