package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.FotoInspecao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FotoInspecaoResponseDTO {

    private UUID id;
    private String caminhoArquivo;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime criadoEm;

    public FotoInspecaoResponseDTO(FotoInspecao foto) {
        this.id = foto.getId();
        this.caminhoArquivo = foto.getCaminhoArquivo();
        this.latitude = foto.getLatitude();
        this.longitude = foto.getLongitude();
        this.criadoEm = foto.getCriadoEm();
    }
}