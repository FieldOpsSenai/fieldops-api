package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.Equipamento;
import br.com.fieldops.api.domain.entity.Local;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.Inspecao;

import java.util.List;

public record SincronizacaoDadosResponseDTO(
    List<ModeloInspecao> modelosPublicados,
    List<Local> locais,
    List<Equipamento> equipamentos,
    List<Inspecao> inspecoesAtribuidas
) {}