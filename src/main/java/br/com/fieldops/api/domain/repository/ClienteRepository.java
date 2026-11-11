package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<Cliente> findByCnpj(String cnpj);

    boolean existsByEmail(String email);

    Optional<Cliente> findByEmail(String email);

    Page<Cliente> findByAtivoTrue(Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.ativo = true AND " +
           "(LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "c.cnpj LIKE CONCAT('%', :termo, '%') OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Cliente> pesquisarAtivos(@Param("termo") String termo, Pageable pageable);
}
