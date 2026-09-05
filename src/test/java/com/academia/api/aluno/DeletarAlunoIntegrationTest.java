package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeletarAlunoIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNO_POR_ID = "/api/alunos/{id}";

    @Test
    @DisplayName("Deve deletar o aluno com sucesso e remover apenas o registro correto do banco")
    @DataSet(value = "datasets/aluno-existente.yml")
    @ExpectedDataSet(value = "datasets/aluno-deletado-esperado.yml", ignoreCols = {"criado_em", "atualizado_em"})
    void deveDeletarAlunoComSucesso() throws Exception {
        mockMvc.perform(delete(URL_ALUNO_POR_ID, 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o ID não existir")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        mockMvc.perform(delete(URL_ALUNO_POR_ID, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Aluno não encontrado com id: 999"));
    }
}
