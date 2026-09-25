package br.com.fieldops.api.infrastructure.security;

import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public DatabaseSeeder(UsuarioRepository usuarioRepository, 
                          PasswordEncoder passwordEncoder, 
                          EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@fieldops.com").isEmpty()) {
            // Procura a referência do Perfil ID 1 (ADMINISTRADOR)
            Perfil perfilAdmin = entityManager.getReference(Perfil.class, 1L);

            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@fieldops.com");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPerfil(perfilAdmin);
            admin.setAtivo(true);

            usuarioRepository.save(admin);
            System.out.println(">>> USUÁRIO ADMIN CRIADO COM SUCESSO! <<<");
        }
    }
}