package br.com.fieldops.fieldops_api;

import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.infrastructure.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // Injeta a secret de teste na propriedade anotada com @Value
        ReflectionTestUtils.setField(tokenService, "secret", "12345678901234567890123456789012");

        // Instancia a Entidade Perfil em vez de chamar o Enum Perfil.ADMINISTRADOR
        Perfil perfilAdmin = new Perfil();
        perfilAdmin.setId(1L);
        perfilAdmin.setNome("ADMINISTRADOR");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@fieldops.com");
        usuario.setSenha("123456");
        usuario.setPerfil(perfilAdmin);
        usuario.setAtivo(true);
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido para o usuário")
    void gerarTokenSucesso() {
        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve extrair o e-mail do subject do token JWT")
    void getSubjectSucesso() {
        String token = tokenService.gerarToken(usuario);
        String subject = tokenService.getSubject(token);

        assertEquals(usuario.getEmail(), subject);
    }

    @Test
    @DisplayName("Deve retornar null ao validar token inválido")
    void getSubjectTokenInvalido() {
        String subject = tokenService.getSubject("token_invalido_qualquer");

        assertNull(subject);
    }
}