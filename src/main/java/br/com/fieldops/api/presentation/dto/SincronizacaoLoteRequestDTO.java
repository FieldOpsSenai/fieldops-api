package br.com.fieldops.api.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SincronizacaoLoteRequestDTO(
    Long inspecaoId,
    Long tecnicoId,
    LocalDateTime dataDispositivo,
    String observacoesGerais,
    List<RespostaItemDTO> respostas
) {
    public record RespostaItemDTO(
        Long itemModeloId,
        String valorResposta,
        String obsItem
    ) {}
}