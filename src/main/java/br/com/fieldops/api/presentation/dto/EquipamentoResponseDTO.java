package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Equipamento;
import lombok.Data;

@Data
public class EquipamentoResponseDTO {

    private Long id;
    private String codigoQr;
    private String numeroSerie;
    private String categoria;
    private String status;
    private Long localId;
    private String localNome;

    public EquipamentoResponseDTO(Equipamento equipamento) {
        this.id = equipamento.getId();
        this.codigoQr = equipamento.getCodigoQr();
        this.numeroSerie = equipamento.getNumeroSerie();
        this.categoria = equipamento.getCategoria();
        this.status = equipamento.getStatus();
        if (equipamento.getLocal() != null) {
            this.localId = equipamento.getLocal().getId();
            this.localNome = equipamento.getLocal().getNome();
        }
    }
}