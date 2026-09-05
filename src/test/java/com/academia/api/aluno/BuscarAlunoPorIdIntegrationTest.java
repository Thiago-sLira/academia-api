package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BuscarAlunoPorIdIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNO_POR_ID = "/api/alunos/{id}";

    @Test
    @DisplayName("Deve retornar 200 e os dados do aluno quando o ID existir")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveRetornarAlunoQuandoIdExistir() throws Exception {
        mockMvc.perform(get(URL_ALUNO_POR_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.telefone").value("11987654321"))
                .andExpect(jsonPath("$.idade").value(25))
                .andExpect(jsonPath("$.peso").value(78.50))
                .andExpect(jsonPath("$.altura").value(1.80))
                .andExpect(jsonPath("$.genero").value("MASCULINO"))
                .andExpect(jsonPath("$.nivelExperiencia").value("INTERMEDIARIO"))
                .andExpect(jsonPath("$.diasDisponiveisSemana").value(4))
                .andExpect(jsonPath("$.restricaoMedica").value("Nenhuma restrição"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o ID não existir")
    @DataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        mockMvc.perform(get(URL_ALUNO_POR_ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Aluno não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 com ErroRespostaDTO quando o ID informado na URL não for um número")
    @DataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoIdNaoForNumerico() throws Exception {
        mockMvc.perform(get("/api/alunos/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsString("'id'")))
                .andExpect(jsonPath("$.erro", containsString("'abc'")))
                .andExpect(jsonPath("$.erro", containsString("número inteiro")));
    }
}
