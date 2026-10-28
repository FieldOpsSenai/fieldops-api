package br.com.fieldops.api.presentation.dto;

import java.util.List;

public record ItemInspecaoRequestDTO(
    String descricao,
    String tipoResposta,
    Integer ordem,
    Boolean obrigatorio,
    Boolean critico
) {}