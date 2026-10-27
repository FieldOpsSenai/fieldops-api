package br.com.fieldops.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respostas_inspecao")
public class RespostaInspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inspecao_id")
    private Inspecao inspecao;

    @Column(name = "item_modelo_id")
    private Long itemModeloId;

    @Column(name = "valor")
    private String valor;

    @Column(name = "observacao")
    private String observacao;

    public RespostaInspecao() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Inspecao getInspecao() { return inspecao; }
    public void setInspecao(Inspecao inspecao) { this.inspecao = inspecao; }

    public Long getItemModeloId() { return itemModeloId; }
    public void setItemModeloId(Long itemModeloId) { this.itemModeloId = itemModeloId; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}