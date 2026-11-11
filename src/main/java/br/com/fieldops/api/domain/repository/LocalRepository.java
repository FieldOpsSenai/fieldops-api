package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {

    List<Local> findByClienteIdAndAtivoTrue(Long clienteId);

    List<Local> findByAtivoTrue();

    Optional<Local> findByIdAndAtivoTrue(Long id);

    boolean existsByClienteIdAndAtivoTrue(Long clienteId);

    Page<Local> findByAtivoTrue(Pageable pageable);

    Page<Local> findByClienteIdAndAtivoTrue(Long clienteId, Pageable pageable);

    @Query("SELECT l FROM Local l WHERE l.ativo = true AND " +
           "(:clienteId IS NULL OR l.cliente.id = :clienteId) AND " +
           "(:termo IS NULL OR LOWER(l.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(l.endereco) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Local> pesquisarAtivos(@Param("termo") String termo, @Param("clienteId") Long clienteId, Pageable pageable);
}
