package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.InspecaoSnapshotItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InspecaoSnapshotItemResponseDTO {

    @Schema(description = "Identificador único do snapshot do item", example = "101")
    private Long id;

    @Schema(description = "Título da seção à qual o item pertence", example = "Parte Elétrica")
    private String secaoTitulo;

    @Schema(description = "Identificador original do item no modelo de inspeção", example = "45")
    private Long itemOriginalId;

    @Schema(description = "Descrição/pergunta a ser verificada", example = "Fiação elétrica em bom estado?")
    private String itemDescricao;

    @Schema(description = "Tipo de resposta esperado", example = "CONFORME_NAO_CONFORME")
    private String tipoResposta;

    @Schema(description = "Ordem de exibição do item", example = "1")
    private Integer ordem;

    @Schema(description = "Indica se o item é de preenchimento obrigatório", example = "true")
    private Boolean obrigatorio;

    @Schema(description = "Indica se o item exige evidência ou é crítico", example = "false")
    private Boolean critico;

    private Boolean evidenciaObrigatoria;

    public InspecaoSnapshotItemResponseDTO(InspecaoSnapshotItem item) {
        this.id = item.getId();
        this.secaoTitulo = item.getSecaoTitulo();
        this.itemOriginalId = item.getItemOriginalId();
        this.itemDescricao = item.getItemDescricao();
        this.tipoResposta = item.getTipoResposta();
        this.ordem = item.getOrdem();
        this.obrigatorio = item.getObrigatorio();
        this.critico = item.getCritico();
        this.evidenciaObrigatoria = item.getEvidenciaObrigatoria();
    }
}

