package com.academia.api.equipamento;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.EquipamentoUpdateRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Atualização de Equipamentos")
class AtualizarEquipamentoIntegrationTest extends BaseIntegrationTest {

    private static final String URL_EQUIPAMENTO_POR_ID = "/api/equipamentos/{id}";

    @Test
    @DisplayName("Deve atualizar todos os campos do equipamento com sucesso")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveAtualizarEquipamentoComSucesso() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "  Remada Curvada  ", "cardio", "Nova descrição", false
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Remada Curvada"))
                .andExpect(jsonPath("$.categoria").value("CARDIO"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"))
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.criadoEm", notNullValue()));
    }

    @Test
    @DisplayName("Deve permitir manter o mesmo nome do próprio equipamento alterando apenas a descrição")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void devePermitirMesmoNomeDoProprioEquipamento() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "Supino Reto", "SUPERIORES", "Descrição alterada", true
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Supino Reto"))
                .andExpect(jsonPath("$.descricao").value("Descrição alterada"));
    }

    @Test
    @DisplayName("Deve permitir manter o próprio nome com capitalização diferente")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void devePermitirMesmoNomeComCapitalizacaoDiferente() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "SUPINO RETO", "SUPERIORES", "Descrição alterada", true
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("SUPINO RETO"))
                .andExpect(jsonPath("$.descricao").value("Descrição alterada"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o equipamento não existir")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar404QuandoEquipamentoNaoExistir() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "Remada Curvada", "SUPERIORES", null, true
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o nome já pertencer a outro equipamento (ignorando maiúsculas/minúsculas)")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar409QuandoNomeDeOutroEquipamento() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "leg press 45", "INFERIORES", null, true
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.erro").value("Já existe outro equipamento cadastrado com este nome"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome estiver em branco")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoNomeEmBranco() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO("  ", "CARDIO", null, true);

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("nome")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome for nulo")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoNomeNulo() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(null, "CARDIO", null, true);

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("nome")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando a categoria for nula")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoCategoriaNula() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO("Esteira", null, null, true);

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("categoria")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'categoria' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 com userHelp quando a categoria for inválida")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoCategoriaInvalida() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO("Esteira", "INEXISTENTE", null, true);

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0].campo").value("categoria"))
                .andExpect(jsonPath("$.detalhes[0].mensagem").value("Valor inválido para o campo 'categoria'"))
                .andExpect(jsonPath("$.detalhes[0].userHelp",
                        containsString("Valores aceitos: [SUPERIORES, INFERIORES, CARDIO, PESO_LIVRE, ACESSORIO]")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o campo ativo for nulo")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoAtivoNulo() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO("Esteira", "CARDIO", null, null);

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("ativo")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'ativo' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando nome e descrição excederem o tamanho máximo")
    @DataSet(value = "datasets/equipamentos-base.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar400QuandoCamposExcederemTamanhoMaximo() throws Exception {
        EquipamentoUpdateRequestDTO requestDTO = new EquipamentoUpdateRequestDTO(
                "a".repeat(101), "CARDIO", "b".repeat(256), true
        );

        mockMvc.perform(put(URL_EQUIPAMENTO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'nome' deve ter no máximo 100 caracteres")))
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'descricao' deve ter no máximo 255 caracteres")));
    }
}
