package com.academia.api.equipamento;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Listagem de Equipamentos")
class ListarEquipamentosIntegrationTest extends BaseIntegrationTest {

    private static final String URL_EQUIPAMENTOS = "/api/equipamentos";

    @Test
    @DisplayName("Deve retornar equipamentos com paginação padrão")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornarEquipamentosComPaginacaoPadrao() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(5)))
                .andExpect(jsonPath("$.totalRegistros").value(5))
                .andExpect(jsonPath("$.equipamentos[0].id").value(1))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Supino Reto"))
                .andExpect(jsonPath("$.equipamentos[0].categoria").value("SUPERIORES"))
                .andExpect(jsonPath("$.equipamentos[0].descricao").value("Banco para supino com barra olímpica"))
                .andExpect(jsonPath("$.equipamentos[0].ativo").value(true))
                .andExpect(jsonPath("$.equipamentos[1].id").value(2))
                .andExpect(jsonPath("$.equipamentos[1].nome").value("Leg Press 45"))
                .andExpect(jsonPath("$.equipamentos[1].categoria").value("INFERIORES"))
                .andExpect(jsonPath("$.equipamentos[2].id").value(3))
                .andExpect(jsonPath("$.equipamentos[2].nome").value("Esteira Ergométrica"))
                .andExpect(jsonPath("$.equipamentos[2].categoria").value("CARDIO"))
                .andExpect(jsonPath("$.equipamentos[3].id").value(4))
                .andExpect(jsonPath("$.equipamentos[3].nome").value("Halter 12kg"))
                .andExpect(jsonPath("$.equipamentos[3].categoria").value("PESO_LIVRE"))
                .andExpect(jsonPath("$.equipamentos[4].id").value(5))
                .andExpect(jsonPath("$.equipamentos[4].nome").value("Cinto de Agachamento"))
                .andExpect(jsonPath("$.equipamentos[4].categoria").value("ACESSORIO"))
                .andExpect(jsonPath("$.equipamentos[4].ativo").value(false));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver equipamentos")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverEquipamentos() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos").isEmpty())
                .andExpect(jsonPath("$.totalRegistros").value(0));
    }

    @Test
    @DisplayName("Deve filtrar equipamentos por nome (busca parcial e case-insensitive)")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveFiltrarEquipamentosPorNome() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("nome", "supino"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(1)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(1))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Supino Reto"))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }

    @Test
    @DisplayName("Deve filtrar equipamentos por categoria com valor exato em maiúsculas")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveFiltrarEquipamentosPorCategoriaMaiuscula() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("categoria", "CARDIO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(1)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(3))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Esteira Ergométrica"))
                .andExpect(jsonPath("$.equipamentos[0].categoria").value("CARDIO"))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }

    @Test
    @DisplayName("Deve filtrar equipamentos por categoria ignorando case (minúsculas)")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveFiltrarEquipamentosPorCategoriaMinuscula() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("categoria", "peso_livre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(1)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(4))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Halter 12kg"))
                .andExpect(jsonPath("$.equipamentos[0].categoria").value("PESO_LIVRE"))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao enviar categoria inválida")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400AoEnviarCategoriaInvalida() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("categoria", "CATEGORIA_INEXISTENTE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsString("Erro de validação")));
    }

    @Test
    @DisplayName("Deve filtrar equipamentos por ID (busca exata)")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveFiltrarEquipamentosPorId() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(1)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(2))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Leg Press 45"))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }

    @Test
    @DisplayName("Deve permitir configurar tamanho da página e consultar segunda página")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void devePermitirPaginacaoCustomizada() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("paginaAtual", "1")
                        .param("tamanhoPagina", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(2)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(3))
                .andExpect(jsonPath("$.equipamentos[1].id").value(4))
                .andExpect(jsonPath("$.totalRegistros").value(5));
    }

    @Test
    @DisplayName("Deve combinar filtros de nome e categoria")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveCombinarFiltrosNomeECategoria() throws Exception {
        mockMvc.perform(get(URL_EQUIPAMENTOS)
                        .param("nome", "Leg")
                        .param("categoria", "INFERIORES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipamentos", hasSize(1)))
                .andExpect(jsonPath("$.equipamentos[0].id").value(2))
                .andExpect(jsonPath("$.equipamentos[0].nome").value("Leg Press 45"))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }
}
