package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.SecaoInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecaoInspecaoRepository extends JpaRepository<SecaoInspecao, Long> {

    List<SecaoInspecao> findByModeloIdOrderByOrdemAsc(Long modeloId);
}

