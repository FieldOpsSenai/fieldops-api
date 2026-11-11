package br.com.fieldops.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itens_inspecao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descricao;

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
    @JoinColumn(name = "secao_id", nullable = false)
    @JsonBackReference
    private SecaoInspecao secao;
}
