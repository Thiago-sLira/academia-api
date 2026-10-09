
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
    @DisplayName("Deve retornar alunos com paginação padrão")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveRetornarAlunosComPaginacaoPadrao() throws Exception {
        mockMvc.perform(get(URL_ALUNOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("João da Silva"))
                .andExpect(jsonPath("$.content[0].email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.content[0].genero").value("MASCULINO"))
                .andExpect(jsonPath("$.content[0].nivelExperiencia").value("INTERMEDIARIO"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].nome").value("Maria Souza"))
                .andExpect(jsonPath("$.content[1].email").value("maria.souza@email.com"))
                .andExpect(jsonPath("$.content[1].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não houver alunos")
    @DataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornarPaginaVaziaQuandoNaoHouverAlunos() throws Exception {
        mockMvc.perform(get(URL_ALUNOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo nome")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorNome() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("nome", "Maria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Maria Souza"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo email")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorEmail() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("email", "joao.silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo ID")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorId() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    @DisplayName("Deve permitir configurar tamanho da página")
    @DataSet(value = "datasets/aluno-existente.yml")
    void devePermitirConfigurarTamanhoDaPagina() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("pagina", "0")
                        .param("tamanhoPagina", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @DisplayName("Deve permitir consultar a segunda página")
    @DataSet(value = "datasets/aluno-existente.yml")
    void devePermitirConsultarSegundaPagina() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("pagina", "1")
                        .param("tamanhoPagina", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.number").value(1));
    }

    @Test
    @DisplayName("Deve combinar filtros de nome e gênero")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveCombinarFiltros() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("nome", "João")
                        .param("genero", "MASCULINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("João da Silva"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo telefone")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorTelefone() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("telefone", "11987654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pela idade")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorIdade() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("idade", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo peso")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorPeso() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("peso", "78.50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pela altura")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorAltura() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("altura", "1.80"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo gênero")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorGenero() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("genero", "MASCULINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Deve filtrar alunos pelo nível de experiência")
    @DataSet(value = "datasets/aluno-existente.yml")
    void deveFiltrarAlunosPorNivelExperiencia() throws Exception {
        mockMvc.perform(get(URL_ALUNOS)
                        .param("nivelExperiencia", "INTERMEDIARIO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }
}