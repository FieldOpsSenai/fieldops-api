package br.com.fieldops.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ErroResponseDTO {

    @Schema(description = "Título ou categoria legível do erro", example = "Acesso Negado")
    private String erro;

    @Schema(description = "Mensagem detalhada do erro retornado pela API", example = "Usuário não possui permissão para acessar este recurso")
    private String mensagem;

    @Schema(description = "Data e hora exata em que o erro ocorreu", example = "2026-09-30T09:54:35")
    private LocalDateTime timestamp;

    @Schema(description = "Código de status HTTP da resposta", example = "403")
    private int status;

    public ErroResponseDTO(String erro, String mensagem, int status) {
        this.erro = erro;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }
}