package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<Cliente> findByCnpj(String cnpj);
}