package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.StatusInspecao;
import br.com.fieldops.api.domain.enums.PrioridadeInspecao;
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

    private LocalDateTime dataLimite;

    private PrioridadeInspecao prioridade;

    private String instrucoes;

    private Long modeloId;
    private Integer modeloVersao;
    private Long clienteId;
    private String clienteNome;
    private Long localId;
    private String localNome;

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
        this.dataLimite = inspecao.getDataLimite();
        this.prioridade = inspecao.getPrioridade();
        this.instrucoes = inspecao.getInstrucoes();
        this.observacoes = inspecao.getObservacoes();
        
        if (inspecao.getEquipamento() != null) {
            this.equipamentoId = inspecao.getEquipamento().getId();
            this.equipamentoNome = inspecao.getEquipamento().getNome();
            if (inspecao.getEquipamento().getLocal() != null) {
                this.localId = inspecao.getEquipamento().getLocal().getId();
                this.localNome = inspecao.getEquipamento().getLocal().getNome();
                if (inspecao.getEquipamento().getLocal().getCliente() != null) {
                    this.clienteId = inspecao.getEquipamento().getLocal().getCliente().getId();
                    this.clienteNome = inspecao.getEquipamento().getLocal().getCliente().getNome();
                }
            }
        }
        if (inspecao.getModeloInspecao() != null) {
            this.modeloId = inspecao.getModeloInspecao().getId();
            this.modeloVersao = inspecao.getModeloInspecao().getVersao();
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

    public LocalDateTime getDataLimite() { return dataLimite; }
    public PrioridadeInspecao getPrioridade() { return prioridade; }
    public String getInstrucoes() { return instrucoes; }
    public Long getModeloId() { return modeloId; }
    public Integer getModeloVersao() { return modeloVersao; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getLocalId() { return localId; }
    public String getLocalNome() { return localNome; }

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
