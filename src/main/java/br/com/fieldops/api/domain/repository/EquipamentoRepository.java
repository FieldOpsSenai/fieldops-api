package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByLocalId(Long localId);

    List<Equipamento> findByStatus(String status);
}