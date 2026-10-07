package com.academia.api.tipotreino;

import com.academia.api.BaseIntegrationTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Listar Tipos de Treino")
class ListarTiposTreinoIntegrationTest extends BaseIntegrationTest {

    private static final String URL = "/api/tipos-treino";

    @Test
    @DisplayName("Deve retornar 200 com a lista de tipos quando existirem registros")
    @DataSet(value = "datasets/tipos-treino-base.yml")
    void deveRetornarListaComTiposQuandoExistiremRegistros() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Musculação"))
                .andExpect(jsonPath("$[0].descricao").value("Treino com pesos e equipamentos de resistência"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Cardio"))
                .andExpect(jsonPath("$[1].descricao").value("Treino aeróbico para condicionamento cardiovascular"));
    }

    @Test
    @DisplayName("Deve retornar 200 com lista vazia quando não houver tipos cadastrados")
    @DataSet(value = "datasets/tipos-treino-vazio.yml")
    void deveRetornarListaVaziaQuandoNaoHouverTipos() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
