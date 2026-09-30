package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @Schema(description = "Refresh token UUID enviado após login para renovar sessão", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        @NotBlank(message = "O refresh token é obrigatório")
        String refreshToken
) {}