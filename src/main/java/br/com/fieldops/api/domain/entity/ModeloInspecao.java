package br.com.fieldops.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modelos_inspecao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloInspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false)
    private Integer versao = 1;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SecaoInspecao> secoes = new ArrayList<>();
}