package br.com.fieldops.fieldops_api;

import br.com.fieldops.api.FieldopsApiApplication; // <-- Importa a classe principal do outro pacote

import br.com.fieldops.api.domain.entity.Perfil;
import br.com.fieldops.api.domain.entity.RefreshToken;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.repository.RefreshTokenRepository;
import br.com.fieldops.api.domain.repository.UsuarioRepository;
import br.com.fieldops.api.domain.service.RefreshTokenService;
import br.com.fieldops.api.presentation.dto.RefreshRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FieldopsApiApplication.class) // <-- Aponta onde o Spring deve buscar a configuração
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RefreshTokenIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste.refresh@fieldops.com");
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setPerfil(Perfil.ADMINISTRADOR);
        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve renovar a sessão com sucesso enviando um Refresh Token válido")
    void deveRenovarSessaoComSucesso() throws Exception {
        RefreshToken refreshToken = refreshTokenService.criarRefreshToken(usuario);
        RefreshRequestDTO dto = new RefreshRequestDTO(refreshToken.getToken());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("Deve retornar 401 ao tentar renovar com Refresh Token inexistente")
    void deveRetornar401ComTokenInexistente() throws Exception {
        RefreshRequestDTO dto = new RefreshRequestDTO(UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 ao tentar renovar com Refresh Token expirado")
    void deveRetornar401ComTokenExpirado() throws Exception {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataExpiracao(Instant.now().minusSeconds(3600));
        refreshToken.setRevogado(false);
        refreshTokenRepository.save(refreshToken);

        RefreshRequestDTO dto = new RefreshRequestDTO(refreshToken.getToken());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}