package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioUpdateDTO {

    @Schema(description = "Nome completo do usuário", example = "Carlos Eduardo")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "E-mail de acesso do usuário", example = "carlos.eduardo@fieldops.com.br")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Schema(description = "Perfil de acesso do usuário no sistema", example = "ADMINISTRADOR")
    @NotNull(message = "O perfil é obrigatório")
    private Perfil perfil;

    @Schema(description = "Nova senha de acesso (opcional na edição)", example = "NovaSenha@2026")
    private String senha;

    public UsuarioUpdateDTO() {
    }

    public UsuarioUpdateDTO(String nome, String email, Perfil perfil, String senha) {
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}