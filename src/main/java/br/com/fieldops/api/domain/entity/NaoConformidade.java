package br.com.fieldops.api.domain.entity;

import br.com.fieldops.api.domain.enums.CriticidadeNaoConformidade;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nao_conformidades")
public class NaoConformidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "criticidade", nullable = false, length = 50)
    private CriticidadeNaoConformidade criticidade;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro = LocalDateTime.now();

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspecao_id", nullable = false)
    private Inspecao inspecao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_id")
    private RespostaInspecao resposta;

    public NaoConformidade() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CriticidadeNaoConformidade getCriticidade() { return criticidade; }
    public void setCriticidade(CriticidadeNaoConformidade criticidade) { this.criticidade = criticidade; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Inspecao getInspecao() { return inspecao; }
    public void setInspecao(Inspecao inspecao) { this.inspecao = inspecao; }

    public RespostaInspecao getResposta() { return resposta; }
    public void setResposta(RespostaInspecao resposta) { this.resposta = resposta; }
}