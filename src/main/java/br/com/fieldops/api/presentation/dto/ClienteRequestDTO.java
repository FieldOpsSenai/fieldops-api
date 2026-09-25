package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank(message = "A razão social é obrigatória")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
    private String razaoSocial;

    @Size(max = 20, message = "O CNPJ deve ter no máximo 20 caracteres")
    private String cnpj;

    @NotBlank(message = "O contato é obrigatório")
    @Size(max = 100, message = "O contato deve ter no máximo 100 caracteres")
    private String contato;
}