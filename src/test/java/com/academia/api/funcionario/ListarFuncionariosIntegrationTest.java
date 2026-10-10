
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
    @DisplayName("Deve retornar funcionários com paginação padrão")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveRetornarListaComFuncionariosQuandoExistiremRegistros() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funcionarios.length()").value(2))
                .andExpect(jsonPath("$.totalRegistros").value(2))
                .andExpect(jsonPath("$.funcionarios[0].id").value(1))
                .andExpect(jsonPath("$.funcionarios[0].nome").value("Admin Teste"))
                .andExpect(jsonPath("$.funcionarios[0].email").value("admin@academia.com"))
                .andExpect(jsonPath("$.funcionarios[0].perfil").value("ADMIN"))
                .andExpect(jsonPath("$.funcionarios[0].ativo").value(true))
                .andExpect(jsonPath("$.funcionarios[1].id").value(2))
                .andExpect(jsonPath("$.funcionarios[1].nome").value("Professor Silva"))
                .andExpect(jsonPath("$.funcionarios[1].email").value("professor.silva@academia.com"))
                .andExpect(jsonPath("$.funcionarios[1].perfil").value("PROFESSOR"))
                .andExpect(jsonPath("$.funcionarios[1].ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver funcionários")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverFuncionarios() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funcionarios").isEmpty())
                .andExpect(jsonPath("$.totalRegistros").value(0));
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

    @Test
    @DisplayName("Deve filtrar funcionários por ID")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveFiltrarFuncionariosPorId() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(1))
                .andExpect(jsonPath("$.funcionarios[0].nome").value("Admin Teste"));
    }

    @Test
    @DisplayName("Deve filtrar funcionários por nome")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveFiltrarFuncionariosPorNome() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("nome", "silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].nome").value("Professor Silva"));
    }

    @Test
    @DisplayName("Deve filtrar funcionários por email")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveFiltrarFuncionariosPorEmail() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("email", "ADMIN@"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].email").value("admin@academia.com"));
    }

    @Test
    @DisplayName("Deve filtrar funcionários por registro acadêmico")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveFiltrarFuncionariosPorRegistroAcademico() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("registroAcademico", "ADM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar funcionários por perfil")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveFiltrarFuncionariosPorPerfil() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("perfil", "PROFESSOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(2));
    }

    @Test
    @DisplayName("Deve filtrar funcionários ativos")
    @DataSet(value = "datasets/funcionario-com-inativo.yml")
    void deveFiltrarFuncionariosAtivos() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("ativo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(1))
                .andExpect(jsonPath("$.funcionarios[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve aplicar tamanho de página personalizado")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveAplicarTamanhoPaginaPersonalizado() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("tamanhoPagina", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funcionarios.length()").value(1))
                .andExpect(jsonPath("$.totalRegistros").value(2));
    }

    @Test
    @DisplayName("Deve retornar segunda página de funcionários")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveRetornarSegundaPagina() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS)
                        .param("paginaAtual", "1")
                        .param("tamanhoPagina", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funcionarios.length()").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(2));
    }

    @Test
    @DisplayName("Deve filtrar funcionários inativos")
    @DataSet(value = "datasets/funcionario-com-inativo.yml")
    void deveFiltrarFuncionariosInativos() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS).param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios[0].ativo").value(false));
    }

    @Test
    @DisplayName("Deve combinar filtros de nome e perfil")
    @DataSet(value = "datasets/funcionario-existente.yml")
    void deveCombinarFiltrosNomeEPerfil() throws Exception {
        mockMvc.perform(get(URL_FUNCIONARIOS)
                        .param("nome", "Professor")
                        .param("perfil", "PROFESSOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(1))
                .andExpect(jsonPath("$.funcionarios.length()").value(1))
                .andExpect(jsonPath("$.funcionarios[0].id").value(2))
                .andExpect(jsonPath("$.funcionarios[0].nome").value("Professor Silva"))
                .andExpect(jsonPath("$.funcionarios[0].perfil").value("PROFESSOR"));
    }
}
