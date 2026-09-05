package com.academia.api.funcionario;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListarFuncionariosIntegrationTest extends BaseIntegrationTest {

    private static final String URL_FUNCIONARIOS = "/api/funcionarios";
    private static final String URL_FUNCIONARIOS_ATIVOS = "/api/funcionarios/ativos";

    @Test
    @DisplayName("Deve retornar lista com todos os funcionários quando existirem registros")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveRetornarListaComFuncionariosQuandoExistiremRegistros() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Admin Teste"))
                .andExpect(jsonPath("$[0].email").value("admin@academia.com"))
                .andExpect(jsonPath("$[0].perfil").value("ADMIN"))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Professor Silva"))
                .andExpect(jsonPath("$[1].email").value("professor.silva@academia.com"))
                .andExpect(jsonPath("$[1].perfil").value("PROFESSOR"))
                .andExpect(jsonPath("$[1].ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver funcionários cadastrados")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverFuncionarios() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Deve retornar apenas os funcionários ativos quando houver inativos")
    @DataSet(value = "datasets/funcionario-com-inativo.yml")
    void deveRetornarApenasAtivosQuandoHouverInativos() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS_ATIVOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista vazia de ativos quando não houver funcionários")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornarListaVaziaDeAtivos() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS_ATIVOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
