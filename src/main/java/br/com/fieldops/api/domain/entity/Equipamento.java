package br.com.fieldops.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "equipamento")
@Entity(name = "Equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @Column(name = "codigo_qr", unique = true, length = 100)
    private String codigoQr;

    @Column(name = "numero_serie", nullable = false, length = 100)
    private String numeroSerie;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false, length = 30)
    private String status;
}