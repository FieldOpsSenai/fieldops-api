package br.com.fieldops.api.presentation.dto;

import br.com.fieldops.api.domain.entity.FotoInspecao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class FotoInspecaoResponseDTO {

    @Schema(description = "Identificador único da foto", example = "50")
    private Long id;

    @Schema(description = "Nome original do arquivo armazenado", example = "foto_compressor_01.jpg")
    private String nomeArquivo;

    @Schema(description = "Tipo do conteúdo (MIME type)", example = "image/jpeg")
    private String tipoConteudo;

    @Schema(description = "Data e hora em que a foto foi vinculada", example = "2026-10-05T10:15:00")
    private LocalDateTime dataUpload;

    @Schema(description = "ID da inspeção associada", example = "101")
    private Long inspecaoId;

    public FotoInspecaoResponseDTO() {
    }

    public FotoInspecaoResponseDTO(FotoInspecao foto) {
        this.id = foto.getId();
        this.nomeArquivo = foto.getNomeArquivo();
        this.tipoConteudo = foto.getTipoConteudo();
        this.dataUpload = foto.getDataUpload();
        if (foto.getInspecao() != null) {
            this.inspecaoId = foto.getInspecao().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoConteudo() {
        return tipoConteudo;
    }

    public void setTipoConteudo(String tipoConteudo) {
        this.tipoConteudo = tipoConteudo;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Long getInspecaoId() {
        return inspecaoId;
    }

    public void setInspecaoId(Long inspecaoId) {
        this.inspecaoId = inspecaoId;
    }
}