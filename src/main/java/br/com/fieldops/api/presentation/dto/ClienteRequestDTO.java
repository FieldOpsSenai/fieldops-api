package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @Schema(description = "Nome do cliente ou razão social", example = "Indústrias Metalúrgicas Silva LTDA")
    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @Schema(description = "CNPJ do cliente com ou sem pontuação", example = "12.345.678/0001-90")
    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(min = 14, max = 20, message = "O CNPJ deve ter um formato válido")
    private String cnpj;

    @Schema(description = "Telefone principal de contato", example = "(15) 99876-5432")
    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Schema(description = "E-mail de contato corporativo", example = "contato@metalurgicasilva.com.br")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    private String email;
}