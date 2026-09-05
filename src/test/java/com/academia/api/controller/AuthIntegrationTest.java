package com.academia.api.controller;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dto.requests.LoginRequestDTO;
import com.academia.api.models.entities.Funcionario;
import com.academia.api.models.enums.PerfilFuncionario;
import com.academia.api.repositories.FuncionarioRepository;
import com.academia.api.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Autenticação e Login")
class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        funcionarioRepository.deleteAll();

        Funcionario funcionario = Funcionario.builder()
                .nome("Admin Teste")
                .email("admin@academia.com")
                .senha(passwordEncoder.encode("senha123"))
                .registroAcademico("ADM-001")
                .perfil(PerfilFuncionario.ADMIN)
                .ativo(true)
                .build();

        funcionarioRepository.save(funcionario);
    }

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar token JWT válido e dados do usuário")
    void deveAutenticarComSucessoERetornarJwt() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@academia.com", "senha123");

        String responseBody = mockMvc.perform(post("/api/funcionarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Admin Teste")))
                .andExpect(jsonPath("$.email", is("admin@academia.com")))
                .andExpect(jsonPath("$.perfil", is("ADMIN")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(responseBody).get("token").asText();

        assertTrue(jwtService.isTokenValido(token));
        assertEquals("admin@academia.com", jwtService.extrairEmail(token));
        assertEquals("ADMIN", jwtService.extrairPerfil(token));
    }

    @Test
    @DisplayName("Deve falhar na autenticação quando a senha estiver incorreta")
    void deveFalharQuandoSenhaIncorreta() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@academia.com", "senhaIncorreta");

        mockMvc.perform(post("/api/funcionarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve falhar na autenticação quando o email não existir")
    void deveFalharQuandoEmailNaoExistir() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("inexistente@academia.com", "senha123");

        mockMvc.perform(post("/api/funcionarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
