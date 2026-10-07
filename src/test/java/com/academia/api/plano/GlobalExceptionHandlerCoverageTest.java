package com.academia.api.plano;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - GlobalExceptionHandler branches de cobertura")
class GlobalExceptionHandlerCoverageTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Deve retornar 415 com o Content-Type recebido na mensagem de erro")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar415ComContentTypeNaMensagemDeErro() throws Exception {
        // Envia sem Content-Type explícito — MockMvc usa application/octet-stream por padrão
        // cobre o branch where ex.getContentType() != null no handleMediaTypeNotSupported
        mockMvc.perform(post("/api/planos-treino")
                        .content("qualquer coisa"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.erro", containsStringIgnoringCase("application/json")));
    }
}
