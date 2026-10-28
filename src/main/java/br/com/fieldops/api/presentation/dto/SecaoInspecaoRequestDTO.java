package br.com.fieldops.api.presentation.dto;

import java.util.List;

public record SecaoInspecaoRequestDTO(
    String titulo,
    Integer ordem,
    List<ItemInspecaoRequestDTO> itens
) {}