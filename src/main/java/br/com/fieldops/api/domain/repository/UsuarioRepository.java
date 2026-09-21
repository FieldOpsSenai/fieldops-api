package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // Método necessário para o UserDetailsService do Spring Security
    UserDetails findUserDetailsByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByAtivoTrue();

    List<Usuario> findByPerfilAndAtivoTrue(Perfil perfil);
}