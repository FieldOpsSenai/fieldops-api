package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.RespostaInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespostaInspecaoRepository extends JpaRepository<RespostaInspecao, Long> {
    Optional<RespostaInspecao> findByInspecaoIdAndItemModeloId(Long inspecaoId, Long itemModeloId);
    List<RespostaInspecao> findByInspecaoId(Long inspecaoId);
}
