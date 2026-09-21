package br.com.fieldops.fieldops_api;

import br.com.fieldops.api.FieldopsApiApplication;
import br.com.fieldops.api.presentation.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FieldopsApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve autenticar com credenciais validas e retornar HTTP 200")
    void deveAutenticarComSucesso() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@fieldops.com");
        dto.setSenha("123456");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.email").value("admin@fieldops.com"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 401 ao tentar logar com senha incorreta")
    void deveRetornar401ComSenhaIncorreta() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@fieldops.com");
        dto.setSenha("senha_errada");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar HTTP 400 ao enviar payload invalido")
    void deveRetornar400ComPayloadInvalido() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("email_invalido");
        dto.setSenha("");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}