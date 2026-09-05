package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AtualizarAlunoIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNO_POR_ID = "/api/alunos/{id}";

    @Test
    @DisplayName("Deve atualizar o aluno com sucesso e persistir os novos dados no banco")
    @DataSet(value = "datasets/aluno-existente.yml")
    @ExpectedDataSet(value = "datasets/aluno-put-esperado.yml", ignoreCols = {"criado_em", "atualizado_em"})
    void deveAtualizarAlunoComSucesso() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "João Atualizado",
                "joao.atualizado@email.com",
                "11911112222",
                30,
                new BigDecimal("85.00"),
                new BigDecimal("1.82"),
                "MASCULINO",
                "AVANCADO",
                5,
                "Lombar"
        );

        mockMvc.perform(put(URL_ALUNO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@email.com"))
                .andExpect(jsonPath("$.telefone").value("11911112222"))
                .andExpect(jsonPath("$.idade").value(30))
                .andExpect(jsonPath("$.peso").value(85.00))
                .andExpect(jsonPath("$.altura").value(1.82))
                .andExpect(jsonPath("$.genero").value("MASCULINO"))
                .andExpect(jsonPath("$.nivelExperiencia").value("AVANCADO"))
                .andExpect(jsonPath("$.diasDisponiveisSemana").value(5))
                .andExpect(jsonPath("$.restricaoMedica").value("Lombar"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o ID não existir")
    @DataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Qualquer Nome",
                "qualquer@email.com",
                "11900000000",
                null, null, null, null, null, null, null
        );

        mockMvc.perform(put(URL_ALUNO_POR_ID, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Aluno não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 e não alterar o banco quando o nome estiver em branco")
    @DataSet(value = "datasets/aluno-existente.yml")
    @ExpectedDataSet(value = "datasets/aluno-existente.yml", ignoreCols = {"criado_em", "atualizado_em"})
    void deveRetornar400ENaoAlterarBancoQuandoNomeEmBranco() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "",
                "joao.silva@email.com",
                "11987654321",
                null, null, null, null, null, null, null
        );

        mockMvc.perform(put(URL_ALUNO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o valor do campo genero for inválido")
    @DataSet(value = "datasets/aluno-existente.yml")
    @ExpectedDataSet(value = "datasets/aluno-existente.yml", ignoreCols = {"criado_em", "atualizado_em"})
    void deveRetornar400QuandoEnumGeneroInvalido() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "João da Silva",
                "joao.silva@email.com",
                "11987654321",
                null, null, null,
                "INVALIDO",
                null, null, null
        );

        mockMvc.perform(put(URL_ALUNO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("genero")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("Valor inválido para o campo 'genero'")));
    }
}
