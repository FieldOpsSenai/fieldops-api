package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRequestDTO {

    @Schema(description = "Nome completo do usuário", example = "Carlos Eduardo")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "E-mail de acesso do usuário", example = "carlos.eduardo@fieldops.com.br")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Schema(description = "Senha de acesso inicial", example = "Senha@123")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @Schema(description = "Perfil de acesso do usuário", example = "ADMINISTRADOR")
    @NotNull(message = "O perfil é obrigatório")
    private Perfil perfil;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String nome, String email, String senha, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}