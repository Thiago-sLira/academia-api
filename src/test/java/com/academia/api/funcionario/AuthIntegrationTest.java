package com.academia.api.funcionario;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.LoginRequestDTO;
import com.academia.api.services.JwtService;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Autenticação e Login")
class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private JwtService jwtService;

    private static final String URL_LOGIN = "/api/funcionarios/login";

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar token JWT válido e dados do usuário")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveAutenticarComSucessoERetornarJwt() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@academia.com", "senha123");

        String responseBody = mockMvc.perform(post(URL_LOGIN)
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
    @DisplayName("Deve retornar 401 quando a senha estiver incorreta")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveFalharQuandoSenhaIncorreta() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@academia.com", "senhaIncorreta");

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("Deve retornar 401 quando o e-mail não existir")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveFalharQuandoEmailNaoExistir() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("inexistente@academia.com", "senha123");

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o e-mail estiver em branco")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveRetornar400QuandoEmailEmBranco() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("", "senha123");

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("email")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha estiver em branco")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveRetornar400QuandoSenhaEmBranco() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@academia.com", "");

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("senha")));
    }

    @Test
    @DisplayName("Deve retornar 415 quando o Content-Type não for application/json")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveRetornar415QuandoContentTypeInvalido() throws Exception {
        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("qualquer coisa"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o corpo da requisição for JSON malformado")
    @DataSet(value = "datasets/funcionarios-base.yml")
    void deveRetornar400QuandoCorpoJsonMalformado() throws Exception {
        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalido }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
