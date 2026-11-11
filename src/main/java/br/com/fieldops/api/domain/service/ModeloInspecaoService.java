package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.ItemInspecao;
import br.com.fieldops.api.domain.entity.ModeloInspecao;
import br.com.fieldops.api.domain.entity.SecaoInspecao;
import br.com.fieldops.api.domain.repository.ItemInspecaoRepository;
import br.com.fieldops.api.domain.repository.ModeloInspecaoRepository;
import br.com.fieldops.api.domain.repository.SecaoInspecaoRepository;
import br.com.fieldops.api.presentation.dto.ItemInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.ModeloInspecaoRequestDTO;
import br.com.fieldops.api.presentation.dto.SecaoInspecaoRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeloInspecaoService {

    private final ModeloInspecaoRepository modeloInspecaoRepository;
    private final SecaoInspecaoRepository secaoInspecaoRepository;
    private final ItemInspecaoRepository itemInspecaoRepository;

    public ModeloInspecaoService(ModeloInspecaoRepository modeloInspecaoRepository,
                                 SecaoInspecaoRepository secaoInspecaoRepository,
                                 ItemInspecaoRepository itemInspecaoRepository) {
        this.modeloInspecaoRepository = modeloInspecaoRepository;
        this.secaoInspecaoRepository = secaoInspecaoRepository;
        this.itemInspecaoRepository = itemInspecaoRepository;
    }

    // PBI-018: Consultas
    @Transactional(readOnly = true)
    public List<ModeloInspecao> listarModelosPublicados() {
        return modeloInspecaoRepository.findModelosPublicados();
    }

    @Transactional(readOnly = true)
    public Page<ModeloInspecao> listarPaginado(String termo, String status, Pageable pageable) {
        return modeloInspecaoRepository.pesquisarModelos(termo, status, pageable);
    }

    @Transactional(readOnly = true)
    public ModeloInspecao buscarPorId(Long id) {
        return modeloInspecaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo de inspeção não encontrado com ID: " + id));
    }

    // PBI-018: Criação de modelo em estado de RASCUNHO
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
                secao.setOrdem(secaoDto.ordem() != null ? secaoDto.ordem() : 1);
                secao.setModelo(modelo);

                if (secaoDto.itens() != null) {
                    List<ItemInspecao> itens = secaoDto.itens().stream().map(itemDto -> {
                        ItemInspecao item = new ItemInspecao();
                        item.setDescricao(itemDto.descricao());
                        item.setTipoResposta(itemDto.tipoResposta());
                        item.setOrdem(itemDto.ordem() != null ? itemDto.ordem() : 1);
                        item.setObrigatorio(itemDto.obrigatorio() != null ? itemDto.obrigatorio() : true);
                        item.setCritico(itemDto.critico() != null ? itemDto.critico() : false);
                        item.setEvidenciaObrigatoria(itemDto.evidenciaObrigatoria() != null ? itemDto.evidenciaObrigatoria() : false);
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

    // PBI-018: Atualização de modelo (bloqueia se já estiver PUBLICADO)
    @Transactional
    public ModeloInspecao atualizarModelo(Long id, ModeloInspecaoRequestDTO dto) {
        ModeloInspecao modelo = buscarPorId(id);

        if ("PUBLICADO".equalsIgnoreCase(modelo.getStatus())) {
            throw new IllegalStateException("Modelos publicados são imutáveis e não podem ser alterados diretamente.");
        }

        modelo.setTitulo(dto.titulo());
        modelo.setCategoria(dto.categoria());

        // Se foram passadas novas seções, substitui respeitando cascata
        if (dto.secoes() != null) {
            modelo.getSecoes().clear();
            for (SecaoInspecaoRequestDTO secaoDto : dto.secoes()) {
                SecaoInspecao secao = new SecaoInspecao();
                secao.setTitulo(secaoDto.titulo());
                secao.setOrdem(secaoDto.ordem() != null ? secaoDto.ordem() : 1);
                secao.setModelo(modelo);

                if (secaoDto.itens() != null) {
                    for (ItemInspecaoRequestDTO itemDto : secaoDto.itens()) {
                        ItemInspecao item = new ItemInspecao();
                        item.setDescricao(itemDto.descricao());
                        item.setTipoResposta(itemDto.tipoResposta());
                        item.setOrdem(itemDto.ordem() != null ? itemDto.ordem() : 1);
                        item.setObrigatorio(itemDto.obrigatorio() != null ? itemDto.obrigatorio() : true);
                        item.setCritico(itemDto.critico() != null ? itemDto.critico() : false);
                        item.setEvidenciaObrigatoria(itemDto.evidenciaObrigatoria() != null ? itemDto.evidenciaObrigatoria() : false);
                        item.setSecao(secao);
                        secao.getItens().add(item);
                    }
                }
                modelo.getSecoes().add(secao);
            }
        }

        return modeloInspecaoRepository.save(modelo);
    }

    // PBI-018: Exclusão de modelo (apenas RASCUNHO)
    @Transactional
    public void excluirModelo(Long id) {
        ModeloInspecao modelo = buscarPorId(id);
        if ("PUBLICADO".equalsIgnoreCase(modelo.getStatus())) {
            throw new IllegalStateException("Modelos publicados são imutáveis e não podem ser excluídos.");
        }
        modeloInspecaoRepository.delete(modelo);
    }

    // PBI-019: Adicionar seção
    @Transactional
    public SecaoInspecao adicionarSecao(Long modeloId, SecaoInspecaoRequestDTO dto) {
        ModeloInspecao modelo = buscarPorId(modeloId);
        if ("PUBLICADO".equalsIgnoreCase(modelo.getStatus())) {
            throw new IllegalStateException("Não é possível adicionar seções em modelos já publicados.");
        }

        SecaoInspecao secao = new SecaoInspecao();
        secao.setTitulo(dto.titulo());
        secao.setOrdem(dto.ordem() != null ? dto.ordem() : modelo.getSecoes().size() + 1);
        secao.setModelo(modelo);

        return secaoInspecaoRepository.save(secao);
    }

    // PBI-019: Editar seção
    @Transactional
    public SecaoInspecao atualizarSecao(Long secaoId, SecaoInspecaoRequestDTO dto) {
        SecaoInspecao secao = secaoInspecaoRepository.findById(secaoId)
                .orElseThrow(() -> new EntityNotFoundException("Seção não encontrada com ID: " + secaoId));

        if ("PUBLICADO".equalsIgnoreCase(secao.getModelo().getStatus())) {
            throw new IllegalStateException("Não é possível alterar seções de modelos já publicados.");
        }

        secao.setTitulo(dto.titulo());
        if (dto.ordem() != null) {
            secao.setOrdem(dto.ordem());
        }

        return secaoInspecaoRepository.save(secao);
    }

    // PBI-019: Excluir seção
    @Transactional
    public void excluirSecao(Long secaoId) {
        SecaoInspecao secao = secaoInspecaoRepository.findById(secaoId)
                .orElseThrow(() -> new EntityNotFoundException("Seção não encontrada com ID: " + secaoId));

        if ("PUBLICADO".equalsIgnoreCase(secao.getModelo().getStatus())) {
            throw new IllegalStateException("Não é possível excluir seções de modelos já publicados.");
        }

        secaoInspecaoRepository.delete(secao);
    }

    // PBI-020 & PBI-021: Adicionar item à seção
    @Transactional
    public ItemInspecao adicionarItem(Long secaoId, ItemInspecaoRequestDTO dto) {
        SecaoInspecao secao = secaoInspecaoRepository.findById(secaoId)
                .orElseThrow(() -> new EntityNotFoundException("Seção não encontrada com ID: " + secaoId));

        if ("PUBLICADO".equalsIgnoreCase(secao.getModelo().getStatus())) {
            throw new IllegalStateException("Não é possível adicionar itens em modelos já publicados.");
        }

        ItemInspecao item = new ItemInspecao();
        item.setDescricao(dto.descricao());
        item.setTipoResposta(dto.tipoResposta());
        item.setOrdem(dto.ordem() != null ? dto.ordem() : secao.getItens().size() + 1);
        item.setObrigatorio(dto.obrigatorio() != null ? dto.obrigatorio() : true);
        item.setCritico(dto.critico() != null ? dto.critico() : false);
        item.setEvidenciaObrigatoria(dto.evidenciaObrigatoria() != null ? dto.evidenciaObrigatoria() : false);
        item.setSecao(secao);

        return itemInspecaoRepository.save(item);
    }

    // PBI-020 & PBI-021: Editar item
    @Transactional
    public ItemInspecao atualizarItem(Long itemId, ItemInspecaoRequestDTO dto) {
        ItemInspecao item = itemInspecaoRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com ID: " + itemId));

        if ("PUBLICADO".equalsIgnoreCase(item.getSecao().getModelo().getStatus())) {
            throw new IllegalStateException("Não é possível alterar itens de modelos já publicados.");
        }

        item.setDescricao(dto.descricao());
        item.setTipoResposta(dto.tipoResposta());
        if (dto.ordem() != null) {
            item.setOrdem(dto.ordem());
        }
        if (dto.obrigatorio() != null) {
            item.setObrigatorio(dto.obrigatorio());
        }
        if (dto.critico() != null) {
            item.setCritico(dto.critico());
        }
        if (dto.evidenciaObrigatoria() != null) {
            item.setEvidenciaObrigatoria(dto.evidenciaObrigatoria());
        }

        return itemInspecaoRepository.save(item);
    }

    // PBI-020: Excluir item
    @Transactional
    public void excluirItem(Long itemId) {
        ItemInspecao item = itemInspecaoRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com ID: " + itemId));

        if ("PUBLICADO".equalsIgnoreCase(item.getSecao().getModelo().getStatus())) {
            throw new IllegalStateException("Não é possível excluir itens de modelos já publicados.");
        }

        itemInspecaoRepository.delete(item);
    }

    // PBI-023: Publicação imutável com validação de estrutura
    @Transactional
    public ModeloInspecao publicarModelo(Long id) {
        ModeloInspecao modelo = buscarPorId(id);

        if ("PUBLICADO".equalsIgnoreCase(modelo.getStatus())) {
            throw new IllegalStateException("O modelo já está publicado.");
        }

        // Critério de aceite: validar se possui estrutura válida antes da publicação
        if (modelo.getSecoes() == null || modelo.getSecoes().isEmpty()) {
            throw new IllegalArgumentException("O modelo deve possuir pelo menos uma seção para ser publicado.");
        }

        boolean possuiItens = modelo.getSecoes().stream()
                .anyMatch(s -> s.getItens() != null && !s.getItens().isEmpty());

        if (!possuiItens) {
            throw new IllegalArgumentException("O modelo deve possuir pelo menos um item de verificação para ser publicado.");
        }

        modelo.setStatus("PUBLICADO");
        return modeloInspecaoRepository.save(modelo);
    }

    // PBI-023: Criação de nova versão a partir de modelo publicado
    @Transactional
    public ModeloInspecao criarNovaVersao(Long id) {
        ModeloInspecao modeloOrigem = buscarPorId(id);

        ModeloInspecao novaVersao = new ModeloInspecao();
        novaVersao.setTitulo(modeloOrigem.getTitulo());
        novaVersao.setCategoria(modeloOrigem.getCategoria());
        novaVersao.setVersao(modeloOrigem.getVersao() + 1);
        novaVersao.setStatus("RASCUNHO");
        novaVersao.setDataCriacao(LocalDateTime.now());

        List<SecaoInspecao> novasSecoes = new ArrayList<>();
        if (modeloOrigem.getSecoes() != null) {
            for (SecaoInspecao secaoOrigem : modeloOrigem.getSecoes()) {
                SecaoInspecao novaSecao = new SecaoInspecao();
                novaSecao.setTitulo(secaoOrigem.getTitulo());
                novaSecao.setOrdem(secaoOrigem.getOrdem());
                novaSecao.setModelo(novaVersao);

                List<ItemInspecao> novosItens = new ArrayList<>();
                if (secaoOrigem.getItens() != null) {
                    for (ItemInspecao itemOrigem : secaoOrigem.getItens()) {
                        ItemInspecao novoItem = new ItemInspecao();
                        novoItem.setDescricao(itemOrigem.getDescricao());
                        novoItem.setTipoResposta(itemOrigem.getTipoResposta());
                        novoItem.setOrdem(itemOrigem.getOrdem());
                        novoItem.setObrigatorio(itemOrigem.getObrigatorio());
                        novoItem.setCritico(itemOrigem.getCritico());
                        novoItem.setSecao(novaSecao);
                        novosItens.add(novoItem);
                    }
                }
                novaSecao.setItens(novosItens);
                novasSecoes.add(novaSecao);
            }
        }
        novaVersao.setSecoes(novasSecoes);

        return modeloInspecaoRepository.save(novaVersao);
    }
}
