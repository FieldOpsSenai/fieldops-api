package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.RegistroSincronizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroSincronizacaoRepository extends JpaRepository<RegistroSincronizacao, Long> {
    boolean existsByChaveIdempotencia(String chaveIdempotencia);
}