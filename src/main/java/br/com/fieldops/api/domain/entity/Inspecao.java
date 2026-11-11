package br.com.fieldops.api.domain.entity;

import br.com.fieldops.api.domain.enums.PrioridadeInspecao;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeInspecao prioridade = PrioridadeInspecao.MEDIA;

    @Column(name = "data_agendada", nullable = false)
    private LocalDateTime dataAgendada;

    @Column(name = "data_limite")
    private LocalDateTime dataLimite;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @Column(length = 1000)
    private String instrucoes;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "precisao")
    private Double precisao;

    private String observacoes;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Column(name = "data_revisao")
    private LocalDateTime dataRevisao;

    @Column(name = "motivo_reprovacao")
    private String motivoReprovacao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @Column(name = "cancelado_por_usuario_id")
    private Long canceladoPorUsuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_inspecao_id")
    private ModeloInspecao modeloInspecao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "inspecao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InspecaoSnapshotItem> itensSnapshot = new ArrayList<>();
}
