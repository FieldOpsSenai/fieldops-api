package br.com.fieldops.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "inspecoes")
@Entity(name = "Inspecao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Inspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInspecao status;

    @Column(name = "data_agendada", nullable = false)
    private LocalDateTime dataAgendada;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "precisao")
    private Double precisao;

    private String observacoes;

    // Novos campos de auditoria e revisão mapeados com a tabela do banco
    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Column(name = "data_revisao")
    private LocalDateTime dataRevisao;

    @Column(name = "motivo_reprovacao")
    private String motivoReprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}