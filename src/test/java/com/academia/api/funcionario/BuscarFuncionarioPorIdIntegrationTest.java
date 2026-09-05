package com.academia.api.funcionario;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BuscarFuncionarioPorIdIntegrationTest extends BaseIntegrationTest {

    private static final String URL_FUNCIONARIO_POR_ID = "/api/funcionarios/{id}";

    @Test
    @DisplayName("Deve retornar 200 e os dados do funcionário quando o ID existir")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveRetornarFuncionarioQuandoIdExistir() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIO_POR_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Admin Teste"))
                .andExpect(jsonPath("$.email").value("admin@academia.com"))
                .andExpect(jsonPath("$.registroAcademico").value("ADM-001"))
                .andExpect(jsonPath("$.perfil").value("ADMIN"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o ID não existir")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIO_POR_ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Funcionário não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 com ErroRespostaDTO quando o ID informado na URL não for um número")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/api/funcionarios/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsString("'id'")))
                .andExpect(jsonPath("$.erro", containsString("'abc'")))
                .andExpect(jsonPath("$.erro", containsString("número inteiro")));
    }
}
