package br.com.fieldops.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "secoes_inspecao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecaoInspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false)
    private Integer ordem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    @JsonBackReference
    private ModeloInspecao modelo;

    @OneToMany(mappedBy = "secao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemInspecao> itens = new ArrayList<>();
}