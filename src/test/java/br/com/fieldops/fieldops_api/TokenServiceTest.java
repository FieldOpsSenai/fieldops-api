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
        ReflectionTestUtils.setField(tokenService, "secret", "minha-chave-secreta-muito-segura-para-testes-fieldops-123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Desenvolvedor Teste");
        usuario.setEmail("dev@fieldops.com");
        usuario.setPerfil(Perfil.ADMINISTRADOR);
    }

    @Test
    @DisplayName("Deve gerar token JWT valido com sucesso")
    void deveGerarTokenComSucesso() {
        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.trim().isEmpty());
    }

    @Test
    @DisplayName("Deve extrair o subject (email) do token gerado")
    void deveValidarEObterSubjectDoToken() {
        String token = tokenService.gerarToken(usuario);
        String subject = tokenService.getSubject(token);

        assertEquals("dev@fieldops.com", subject);
    }
}