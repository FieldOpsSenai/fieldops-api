package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.ModeloInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloInspecaoRepository extends JpaRepository<ModeloInspecao, Long> {

    @Query("SELECT m FROM ModeloInspecao m WHERE m.status = 'PUBLICADO'")
    List<ModeloInspecao> findModelosPublicados();
}