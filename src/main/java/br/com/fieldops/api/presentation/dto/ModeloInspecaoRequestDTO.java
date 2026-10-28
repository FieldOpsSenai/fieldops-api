package br.com.fieldops.api.presentation.dto;

import java.util.List;

public record ModeloInspecaoRequestDTO(
    String titulo,
    String categoria,
    List<SecaoInspecaoRequestDTO> secoes
) {}