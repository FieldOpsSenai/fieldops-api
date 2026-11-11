package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.ItemInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemInspecaoRepository extends JpaRepository<ItemInspecao, Long> {

    List<ItemInspecao> findBySecaoIdOrderByOrdemAsc(Long secaoId);
}

