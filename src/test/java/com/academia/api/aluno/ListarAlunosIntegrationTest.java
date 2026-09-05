package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListarAlunosIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNOS = "/api/alunos";

    @Test
    @DisplayName("Deve retornar lista com todos os alunos quando existirem registros")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveRetornarListaComAlunosQuandoExistiremRegistros() throws Exception {
        mockMvc.perform(get(URL_ALUNOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João da Silva"))
                .andExpect(jsonPath("$[0].email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$[0].genero").value("MASCULINO"))
                .andExpect(jsonPath("$[0].nivelExperiencia").value("INTERMEDIARIO"))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Maria Souza"))
                .andExpect(jsonPath("$[1].email").value("maria.souza@email.com"))
                .andExpect(jsonPath("$[1].ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver alunos cadastrados")
    @DataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverAlunos() throws Exception {
        mockMvc.perform(get(URL_ALUNOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
