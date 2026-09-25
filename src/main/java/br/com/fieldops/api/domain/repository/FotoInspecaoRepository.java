package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.FotoInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FotoInspecaoRepository extends JpaRepository<FotoInspecao, UUID> {

    List<FotoInspecao> findByInspecaoId(UUID inspecaoId);
}