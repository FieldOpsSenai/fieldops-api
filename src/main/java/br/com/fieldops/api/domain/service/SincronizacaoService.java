package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.RegistroSincronizacao;
import br.com.fieldops.api.domain.entity.RespostaInspecao;
import br.com.fieldops.api.domain.entity.StatusInspecao;
import br.com.fieldops.api.domain.repository.EquipamentoRepository;
import br.com.fieldops.api.domain.repository.InspecaoRepository;
import br.com.fieldops.api.domain.repository.LocalRepository;
import br.com.fieldops.api.domain.repository.ModeloInspecaoRepository;
import br.com.fieldops.api.domain.repository.RegistroSincronizacaoRepository;
import br.com.fieldops.api.domain.repository.RespostaInspecaoRepository;
import br.com.fieldops.api.presentation.dto.SincronizacaoDadosResponseDTO;
import br.com.fieldops.api.presentation.dto.SincronizacaoLoteRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SincronizacaoService {

    private final ModeloInspecaoRepository modeloInspecaoRepository;
    private final LocalRepository localRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final InspecaoRepository inspecaoRepository;
    private final RespostaInspecaoRepository respostaInspecaoRepository;
    private final RegistroSincronizacaoRepository registroSincronizacaoRepository;

    public SincronizacaoService(ModeloInspecaoRepository modeloInspecaoRepository,
                                LocalRepository localRepository,
                                EquipamentoRepository equipamentoRepository,
                                InspecaoRepository inspecaoRepository,
                                RespostaInspecaoRepository respostaInspecaoRepository,
                                RegistroSincronizacaoRepository registroSincronizacaoRepository) {
        this.modeloInspecaoRepository = modeloInspecaoRepository;
        this.localRepository = localRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.inspecaoRepository = inspecaoRepository;
        this.respostaInspecaoRepository = respostaInspecaoRepository;
        this.registroSincronizacaoRepository = registroSincronizacaoRepository;
    }

    @Transactional(readOnly = true)
    public SincronizacaoDadosResponseDTO baixarDadosIniciais(Long usuarioId) {
        var modelos = modeloInspecaoRepository.findModelosPublicados();
        var locais = localRepository.findAll();
        var equipamentos = equipamentoRepository.findAll();
        
        // Pega apenas as inspeções atribuídas a este técnico específico em campo
        var inspecoes = inspecaoRepository.findByUsuarioId(usuarioId);

        return new SincronizacaoDadosResponseDTO(modelos, locais, equipamentos, inspecoes);
    }

    @Transactional
    public void processarLoteOffline(String idempotencyKey, SincronizacaoLoteRequestDTO loteDto) {
        // 1. Validação de Idempotência (RN-067 / Teste 10)
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            boolean jaProcessado = registroSincronizacaoRepository.existsByChaveIdempotencia(idempotencyKey);
            if (jaProcessado) {
                return; // Já processado anteriormente, ignora para evitar duplicidade
            }
        }

        // 2. Localiza a inspeção correspondente
        Inspecao inspecao = inspecaoRepository.findById(loteDto.inspecaoId())
                .orElseThrow(() -> new IllegalArgumentException("Inspeção não encontrada com ID: " + loteDto.inspecaoId()));

        // 3. Atualiza dados da inspeção com controle duplo de data/hora (RN-034 / Teste 09)
        inspecao.setObservacoes(loteDto.observacoesGerais());
        inspecao.setDataRealizacao(loteDto.dataDispositivo()); // Data gerada offline no dispositivo móvel
        inspecao.setStatus(StatusInspecao.CONCLUIDA);
        inspecaoRepository.save(inspecao);

        // 4. Salva as respostas dos itens do checklist enviadas no lote offline
        if (loteDto.respostas() != null) {
            for (var respDto : loteDto.respostas()) {
                RespostaInspecao resposta = new RespostaInspecao();
                resposta.setInspecao(inspecao);
                resposta.setItemModeloId(respDto.itemModeloId());
                resposta.setValor(respDto.valorResposta());
                resposta.setObservacao(respDto.obsItem());
                
                respostaInspecaoRepository.save(resposta);
            }
        }

        // 5. Registra a chave de idempotência para bloquear futuros reenvios do mesmo pacote
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            RegistroSincronizacao registro = new RegistroSincronizacao();
            registro.setChaveIdempotencia(idempotencyKey);
            registro.setDataRecebimento(LocalDateTime.now()); // Data em que o servidor processou
            registro.setUsuarioId(inspecao.getUsuario().getId()); // Atribuído corretamente para atender a restrição do banco
            registroSincronizacaoRepository.save(registro);
        }
    }
}