package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.ItemInspecao;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.SecaoInspecao;
import br.com.fieldops.api.domain.repository.ModeloInspecaoRepository;
import br.com.fieldops.api.presentation.dto.ModeloInspecaoRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeloInspecaoService {

    private final ModeloInspecaoRepository modeloInspecaoRepository;

    public ModeloInspecaoService(ModeloInspecaoRepository modeloInspecaoRepository) {
        this.modeloInspecaoRepository = modeloInspecaoRepository;
    }

    @Transactional(readOnly = true)
    public List<ModeloInspecao> listarModelosPublicados() {
        return modeloInspecaoRepository.findModelosPublicados();
    }

    @Transactional
    public ModeloInspecao criarModelo(ModeloInspecaoRequestDTO dto) {
        ModeloInspecao modelo = new ModeloInspecao();
        modelo.setTitulo(dto.titulo());
        modelo.setCategoria(dto.categoria());
        modelo.setVersao(1);
        modelo.setStatus("RASCUNHO");
        modelo.setDataCriacao(LocalDateTime.now());

        if (dto.secoes() != null) {
            List<SecaoInspecao> secoes = dto.secoes().stream().map(secaoDto -> {
                SecaoInspecao secao = new SecaoInspecao();
                secao.setTitulo(secaoDto.titulo());
                secao.setOrdem(secaoDto.ordem());
                secao.setModelo(modelo);

                if (secaoDto.itens() != null) {
                    List<ItemInspecao> itens = secaoDto.itens().stream().map(itemDto -> {
                        ItemInspecao item = new ItemInspecao();
                        item.setDescricao(itemDto.descricao());
                        item.setTipoResposta(itemDto.tipoResposta());
                        item.setOrdem(itemDto.ordem());
                        item.setObrigatorio(itemDto.obrigatorio() != null ? itemDto.obrigatorio() : true);
                        item.setCritico(itemDto.critico() != null ? itemDto.critico() : false);
                        item.setSecao(secao);
                        return item;
                    }).collect(Collectors.toList());
                    secao.setItens(itens);
                }
                return secao;
            }).collect(Collectors.toList());
            modelo.setSecoes(secoes);
        }

        return modeloInspecaoRepository.save(modelo);
    }

    @Transactional
    public ModeloInspecao publicarModelo(Long id) {
        ModeloInspecao modelo = modeloInspecaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo de inspeção não encontrado com ID: " + id));
        
        modelo.setStatus("PUBLICADO");
        return modeloInspecaoRepository.save(modelo);
    }
}