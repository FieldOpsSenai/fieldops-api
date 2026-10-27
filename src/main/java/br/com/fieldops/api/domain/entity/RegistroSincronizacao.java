package br.com.fieldops.api.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_sincronizacao")
public class RegistroSincronizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chave_idempotencia", unique = true, nullable = false)
    private String chaveIdempotencia;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDateTime dataRecebimento;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    public RegistroSincronizacao() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getChaveIdempotencia() { return chaveIdempotencia; }
    public void setChaveIdempotencia(String chaveIdempotencia) { this.chaveIdempotencia = chaveIdempotencia; }

    public LocalDateTime getDataRecebimento() { return dataRecebimento; }
    public void setDataRecebimento(LocalDateTime dataRecebimento) { this.dataRecebimento = dataRecebimento; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}