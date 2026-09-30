package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

public class UsuarioResponseDTO {

    @Schema(description = "Identificador único do usuário no sistema", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Carlos Eduardo")
    private String nome;

    @Schema(description = "E-mail de acesso cadastrado", example = "carlos.eduardo@fieldops.com.br")
    private String email;

    @Schema(description = "Perfil de acesso associado", example = "ADMINISTRADOR")
    private Perfil perfil;

    @Schema(description = "Indica se o usuário está ativo no sistema", example = "true")
    private Boolean ativo;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
        this.ativo = usuario.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}