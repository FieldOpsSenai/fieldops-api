package br.com.fieldops.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank(message = "O refresh token é obrigatório")
        String refreshToken
) {}