package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.NaoConformidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NaoConformidadeRepository extends JpaRepository<NaoConformidade, Long> {
    List<NaoConformidade> findByInspecaoId(Long inspecaoId);
}