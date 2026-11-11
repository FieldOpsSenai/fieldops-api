package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Equipamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByLocalIdAndAtivoTrue(Long localId);

    List<Equipamento> findByAtivoTrue();

    Optional<Equipamento> findByIdAndAtivoTrue(Long id);

    Optional<Equipamento> findByNumeroSerie(String numeroSerie);

    boolean existsByNumeroSerie(String numeroSerie);

    boolean existsByLocalIdAndAtivoTrue(Long localId);

    boolean existsByQrCode(String qrCode);

    Optional<Equipamento> findByQrCodeAndAtivoTrue(String qrCode);

    Page<Equipamento> findByAtivoTrue(Pageable pageable);

    Page<Equipamento> findByLocalIdAndAtivoTrue(Long localId, Pageable pageable);

    @Query("SELECT e FROM Equipamento e WHERE e.ativo = true AND " +
           "(:localId IS NULL OR e.local.id = :localId) AND " +
           "(:termo IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.numeroSerie) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.qrCode) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.tipo) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Equipamento> pesquisarAtivos(@Param("termo") String termo, @Param("localId") Long localId, Pageable pageable);
}
