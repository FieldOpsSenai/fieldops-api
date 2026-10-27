package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.RespostaInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespostaInspecaoRepository extends JpaRepository<RespostaInspecao, Long> {
}