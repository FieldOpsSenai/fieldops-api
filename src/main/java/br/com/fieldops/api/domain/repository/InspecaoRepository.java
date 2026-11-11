package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Inspecao;
import br.com.fieldops.api.domain.entity.StatusInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspecaoRepository extends JpaRepository<Inspecao, Long> {

    List<Inspecao> findByUsuarioId(Long usuarioId);

    List<Inspecao> findByStatus(StatusInspecao status);

    List<Inspecao> findByUsuarioIdAndStatus(Long usuarioId, StatusInspecao status);

    @Query("SELECT i FROM Inspecao i WHERE " +
           "(:termo IS NULL OR LOWER(i.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) OR CAST(i.id AS string) LIKE CONCAT('%', :termo, '%')) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:usuarioId IS NULL OR i.usuario.id = :usuarioId) AND " +
           "(:clienteId IS NULL OR i.equipamento.local.cliente.id = :clienteId) AND " +
           "(:prioridade IS NULL OR i.prioridade = :prioridade)")
    Page<Inspecao> pesquisar(@Param("termo") String termo,
                             @Param("status") StatusInspecao status,
                             @Param("usuarioId") Long usuarioId,
                             @Param("clienteId") Long clienteId,
                             @Param("prioridade") br.com.fieldops.api.domain.enums.PrioridadeInspecao prioridade,
                             Pageable pageable);
}
