package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Equipamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipamentoResponseDTO {

    @Schema(description = "Identificador único do equipamento", example = "1")
    private Long id;

    @Schema(description = "Nome ou modelo do equipamento", example = "Compressor de Ar Parafuso 20HP")
    private String nome;

    @Schema(description = "Número de série do fabricante", example = "CMP-2026-X987")
    private String numeroSerie;

    @Schema(description = "Categoria ou tipo do equipamento", example = "Pneumático")
    private String tipo;

    @Schema(description = "Código QR único do equipamento", example = "EQP-CMP-001")
    private String qrCode;

    @Schema(description = "Status de operação do equipamento", example = "true")
    private Boolean ativo;

    @Schema(description = "ID do local vinculado", example = "10")
    private Long localId;

    @Schema(description = "Nome do local vinculado", example = "Galpão Industrial - Setor A")
    private String localNome;

    public EquipamentoResponseDTO(Equipamento equipamento) {
        this.id = equipamento.getId();
        this.nome = equipamento.getNome();
        this.numeroSerie = equipamento.getNumeroSerie();
        this.tipo = equipamento.getTipo();
        this.qrCode = equipamento.getQrCode();
        this.ativo = equipamento.getAtivo();
        if (equipamento.getLocal() != null) {
            this.localId = equipamento.getLocal().getId();
            this.localNome = equipamento.getLocal().getNome();
        }
    }
}
