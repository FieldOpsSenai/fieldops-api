package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.ModeloInspecao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloInspecaoRepository extends JpaRepository<ModeloInspecao, Long> {

    @Query("SELECT m FROM ModeloInspecao m WHERE m.status = 'PUBLICADO'")
    List<ModeloInspecao> findModelosPublicados();

    List<ModeloInspecao> findByStatus(String status);

    @Query("SELECT m FROM ModeloInspecao m WHERE " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:termo IS NULL OR LOWER(m.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(m.categoria) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<ModeloInspecao> pesquisarModelos(@Param("termo") String termo, @Param("status") String status, Pageable pageable);
}
