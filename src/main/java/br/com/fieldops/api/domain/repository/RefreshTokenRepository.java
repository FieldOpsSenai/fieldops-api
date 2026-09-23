package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.RefreshToken;
import br.com.fieldops.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsuario(Usuario usuario); // <-- MÉTODOS DE DELEÇÃO PARA NORMALIZE O RE-LOGIN
}