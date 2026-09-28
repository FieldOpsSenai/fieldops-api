package br.com.fieldops.fieldops_api;

// Import da classe principal do projeto (ajuste se o nome ou pacote da sua Application for diferente):
import br.com.fieldops.api.FieldopsApiApplication; 

import br.com.fieldops.api.domain.service.UsuarioService;
import br.com.fieldops.api.infrastructure.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FieldopsApiApplication.class) // <-- Resolve a busca do contexto apontando a classe principal
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("Cenário A: Deve retornar 401 Unauthorized quando requisição não contiver token")
    void listarTodos_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "TECNICO")
    @DisplayName("Cenário B: Deve retornar 403 Forbidden quando usuário for TECNICO")
    void listarTodos_ComPerfilTecnico_DeveRetornar403() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.erro").value("Acesso Negado"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Cenário C: Deve retornar 200 OK quando perfil for ADMINISTRADOR")
    void listarTodos_ComPerfilAdministrador_DeveRetornar200() throws Exception {
        given(usuarioService.listarTodos()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}