package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Inspecao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InspecaoResponseDTO {

    private UUID id;
    private Long equipamentoId;
    private String equipamentoNumeroSerie;
    private Long tecnicoId;
    private String tecnicoNome;
    private Long supervisorId;
    private String status;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private BigDecimal latitudeExecucao;
    private BigDecimal longitudeExecucao;
    private String observacoesRevisao;

    public InspecaoResponseDTO(Inspecao inspecao) {
        this.id = inspecao.getId();
        this.status = inspecao.getStatus();
        this.dataAgendamento = inspecao.getDataAgendamento();
        this.dataInicio = inspecao.getDataInicio();
        this.dataConclusao = inspecao.getDataConclusao();
        this.latitudeExecucao = inspecao.getLatitudeExecucao();
        this.longitudeExecucao = inspecao.getLongitudeExecucao();
        this.observacoesRevisao = inspecao.getObservacoesRevisao();

        if (inspecao.getEquipamento() != null) {
            this.equipamentoId = inspecao.getEquipamento().getId();
            this.equipamentoNumeroSerie = inspecao.getEquipamento().getNumeroSerie();
        }

        if (inspecao.getTecnico() != null) {
            this.tecnicoId = inspecao.getTecnico().getId();
            this.tecnicoNome = inspecao.getTecnico().getNome();
        }

        if (inspecao.getSupervisor() != null) {
            this.supervisorId = inspecao.getSupervisor().getId();
        }
    }
}