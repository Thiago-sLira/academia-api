package com.academia.api.plano;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Buscar Plano de Treino por ID")
class BuscarPlanoTreinoPorIdIntegrationTest extends BaseIntegrationTest {

    private static final String URL = "/api/planos-treino/{id}";

    @Test
    @DisplayName("Deve retornar 200 e o idPlanoTreino quando o plano existir")
    @DataSet(value = "datasets/plano-treino-existente.yml")
    void deveRetornarPlanoQuandoIdExistir() throws Exception {
        mockMvc.perform(get(URL, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPlanoTreino").value(1));
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o plano não existir")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar404QuandoPlanoNaoExistir() throws Exception {
        mockMvc.perform(get(URL, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Plano de treino não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o ID informado na URL não for um número")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/api/planos-treino/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsString("'id'")))
                .andExpect(jsonPath("$.erro", containsString("'abc'")));
    }
}
