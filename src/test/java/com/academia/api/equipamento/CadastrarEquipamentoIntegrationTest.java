package com.academia.api.equipamento;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.EquipamentoRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Cadastro de Equipamentos")
class CadastrarEquipamentoIntegrationTest extends BaseIntegrationTest {

    private static final String URL_EQUIPAMENTOS = "/api/equipamentos";

    @Test
    @DisplayName("Deve cadastrar um equipamento com sucesso quando o payload for válido")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    void deveCadastrarEquipamentoComSucesso() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO(
                "Remada Curvada", "SUPERIORES", "Máquina de remada para dorsais", false
        );

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Remada Curvada"))
                .andExpect(jsonPath("$.categoria").value("SUPERIORES"))
                .andExpect(jsonPath("$.descricao").value("Máquina de remada para dorsais"))
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.criadoEm", notNullValue()));
    }

    @Test
    @DisplayName("Deve assumir ativo = true e aceitar descrição nula quando não informados")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    void deveAssumirAtivoTrueQuandoNaoInformado() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO(
                "Esteira", "cardio", null, null
        );

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Esteira"))
                .andExpect(jsonPath("$.categoria").value("CARDIO"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome estiver em branco")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornar400QuandoNomeEmBranco() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO("", "CARDIO", null, null);

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("nome")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome for nulo")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornar400QuandoNomeNulo() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO(null, "CARDIO", null, null);

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando a categoria for nula")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornar400QuandoCategoriaNula() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO("Esteira", null, null, null);

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("categoria")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'categoria' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 com userHelp quando a categoria for inválida")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornar400QuandoCategoriaInvalida() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO("Esteira", "INEXISTENTE", null, null);

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0].campo").value("categoria"))
                .andExpect(jsonPath("$.detalhes[0].mensagem").value("Valor inválido para o campo 'categoria'"))
                .andExpect(jsonPath("$.detalhes[0].userHelp",
                        containsString("Valores aceitos: [SUPERIORES, INFERIORES, CARDIO, PESO_LIVRE, ACESSORIO]")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando nome e descrição excederem o tamanho máximo")
    @DataSet(value = "datasets/equipamentos-vazio.yml")
    @ExpectedDataSet(value = "datasets/equipamentos-vazio.yml")
    void deveRetornar400QuandoCamposExcederemTamanhoMaximo() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO(
                "a".repeat(101), "CARDIO", "b".repeat(256), null
        );

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'nome' deve ter no máximo 100 caracteres")))
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'descricao' deve ter no máximo 255 caracteres")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando já existir equipamento com o mesmo nome (ignorando maiúsculas/minúsculas)")
    @DataSet(value = "datasets/equipamentos-base.yml")
    void deveRetornar409QuandoNomeDuplicado() throws Exception {
        EquipamentoRequestDTO requestDTO = new EquipamentoRequestDTO("supino reto", "SUPERIORES", null, null);

        mockMvc.perform(post(URL_EQUIPAMENTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.erro").value("Já existe um equipamento cadastrado com este nome"));
    }
}
