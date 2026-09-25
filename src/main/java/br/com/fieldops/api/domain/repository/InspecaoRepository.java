package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Inspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InspecaoRepository extends JpaRepository<Inspecao, UUID> {

    List<Inspecao> findByTecnicoId(Long tecnicoId);

    List<Inspecao> findBySupervisorId(Long supervisorId);

    List<Inspecao> findByStatus(String status);

    List<Inspecao> findByTecnicoIdAndStatus(Long tecnicoId, String status);
}