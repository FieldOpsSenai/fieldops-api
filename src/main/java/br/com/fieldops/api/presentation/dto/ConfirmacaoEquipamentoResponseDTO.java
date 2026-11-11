package br.com.fieldops.api.presentation.dto;

public record ConfirmacaoEquipamentoResponseDTO(Long inspecaoId, Long equipamentoId, boolean confirmado, String mensagem) { }
