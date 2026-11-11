package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.RespostaInspecao;

public record RespostaInspecaoResponseDTO(Long id, Long inspecaoId, Long snapshotItemId, String valor, String observacao) {
    public RespostaInspecaoResponseDTO(RespostaInspecao resposta) {
        this(resposta.getId(), resposta.getInspecao().getId(), resposta.getItemModeloId(), resposta.getValor(), resposta.getObservacao());
    }
}
