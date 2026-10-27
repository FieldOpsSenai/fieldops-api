package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.NaoConformidade;
import br.com.fieldops.api.domain.entity.RespostaInspecao;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.NaoConformidadeRepository;
import br.com.fieldops.api.domain.repository.RespostaInspecaoRepository;
import br.com.fieldops.api.presentation.dto.NaoConformidadeRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NaoConformidadeService {

    private final NaoConformidadeRepository naoConformidadeRepository;
    private final InspecaoRepository inspecaoRepository;
    private final RespostaInspecaoRepository respostaInspecaoRepository;

    public NaoConformidadeService(NaoConformidadeRepository naoConformidadeRepository,
                                  InspecaoRepository inspecaoRepository,
                                  RespostaInspecaoRepository respostaInspecaoRepository) {
        this.naoConformidadeRepository = naoConformidadeRepository;
        this.inspecaoRepository = inspecaoRepository;
        this.respostaInspecaoRepository = respostaInspecaoRepository;
    }

    @Transactional
    public NaoConformidade registrar(NaoConformidadeRequestDTO dto) {
        Inspecao inspecao = inspecaoRepository.findById(dto.getInspecaoId())
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com ID: " + dto.getInspecaoId()));

        NaoConformidade nc = new NaoConformidade();
        nc.setCriticidade(dto.getCriticidade());
        nc.setDescricao(dto.getDescricao());
        nc.setDataRegistro(LocalDateTime.now());
        nc.setInspecao(inspecao);

        if (dto.getRespostaId() != null) {
            RespostaInspecao resposta = respostaInspecaoRepository.findById(dto.getRespostaId())
                    .orElseThrow(() -> new RuntimeException("Resposta de inspeção não encontrada com ID: " + dto.getRespostaId()));
            nc.setResposta(resposta);
        }

        return naoConformidadeRepository.save(nc);
    }

    public List<NaoConformidade> listarPorInspecao(Long inspecaoId) {
        return naoConformidadeRepository.findByInspecaoId(inspecaoId);
    }
}