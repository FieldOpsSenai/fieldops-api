package br.com.fieldops.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspecoes_snapshot_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspecaoSnapshotItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secao_titulo", nullable = false, length = 150)
    private String secaoTitulo;

    @Column(name = "item_original_id")
    private Long itemOriginalId;

    @Column(name = "item_descricao", nullable = false, length = 255)
    private String itemDescricao;

    @Column(name = "tipo_resposta", nullable = false, length = 50)
    private String tipoResposta;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean obrigatorio = true;

    @Column(nullable = false)
    private Boolean critico = false;

    @Column(name = "evidencia_obrigatoria", nullable = false)
    private Boolean evidenciaObrigatoria = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspecao_id", nullable = false)
    @JsonBackReference
    private Inspecao inspecao;
}

