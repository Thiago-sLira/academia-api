package com.academia.api.plano;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Listar Planos de Treino")
class ListarPlanosTreinoIntegrationTest extends BaseIntegrationTest {

    private static final String URL = "/api/planos-treino";

    @Test
    @DisplayName("Deve retornar 200 com lista paginada quando existirem planos cadastrados")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveRetornarListaPaginadaQuandoExistiremPlanos() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(12))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Deve aplicar paginação padrão de tamanho 10 quando nenhum parâmetro for informado")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveAplicarPaginacaoPadraoQuandoNenhumParametroInformado() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Deve retornar a segunda página corretamente com os registros restantes")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveRetornarSegundaPaginaComRegistrosRestantes() throws Exception {
        mockMvc.perform(get(URL).param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.totalElements").value(12));
    }

    @Test
    @DisplayName("Deve filtrar por idTipoTreino e retornar apenas planos do tipo informado")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveFiltrarPorIdTipoTreino() throws Exception {
        mockMvc.perform(get(URL).param("idTipoTreino", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(6))
                .andExpect(jsonPath("$.content[0].idTipoTreino").value(1))
                .andExpect(jsonPath("$.content[0].nomeTipoTreino").value("Musculação"));
    }

    @Test
    @DisplayName("Deve filtrar por idProfessorCriador e retornar apenas planos do professor")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveFiltrarPorIdProfessorCriador() throws Exception {
        mockMvc.perform(get(URL).param("idProfessorCriador", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(12))
                .andExpect(jsonPath("$.content[0].idProfessorCriador").value(10))
                .andExpect(jsonPath("$.content[0].nomeProfessor").value("Professor Teste"));
    }

    @Test
    @DisplayName("Deve filtrar por nivelRecomendado e retornar apenas planos do nível informado")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveFiltrarPorNivelRecomendado() throws Exception {
        mockMvc.perform(get(URL).param("nivelRecomendado", "AVANCADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].nivelRecomendado").value("AVANCADO"));
    }

    @Test
    @DisplayName("Deve retornar 200 com content vazio e totalElements 0 quando não houver planos")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverPlanos() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Deve ignorar filtro nivelRecomendado inválido e retornar todos os planos sem aplicar predicate")
    @DataSet(value = "datasets/planos-treino-paginacao.yml")
    void deveIgnorarFiltroNivelRecomendadoInvalido() throws Exception {
        mockMvc.perform(get(URL).param("nivelRecomendado", "NIVEL_DESCONHECIDO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(12));
    }
}
