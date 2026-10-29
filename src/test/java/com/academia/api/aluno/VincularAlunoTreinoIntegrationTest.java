package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.VinculoAlunoTreinoRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Vincular Aluno a Plano de Treino")
class VincularAlunoTreinoIntegrationTest extends BaseIntegrationTest {

    private static final String URL = "/api/alunos-treino";

    private VinculoAlunoTreinoRequestDTO requestValido() {
        return new VinculoAlunoTreinoRequestDTO(1L, 1L, LocalDate.of(2026, 1, 10), null, null);
    }

    @Test
    @DisplayName("Deve vincular aluno ao plano com sucesso e retornar 201 com status ATIVO")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveVincularAlunoAPlanoComSucesso() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVinculo", notNullValue()))
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andExpect(jsonPath("$.idAluno").value(1))
                .andExpect(jsonPath("$.idPlano").value(1));
    }

    @Test
    @DisplayName("Deve retornar 400 quando idAluno for nulo")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar400QuandoIdAlunoForNulo() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                null, 1L, LocalDate.of(2026, 1, 10), null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("idAluno")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando idPlano for negativo")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar400QuandoIdPlanoForNegativo() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, -1L, LocalDate.of(2026, 1, 10), null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("idPlano")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando dataInicio for nula")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar400QuandoDataInicioForNula() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 1L, null, null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("dataInicio")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando observacoes ultrapassar 200 caracteres")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar400QuandoObservacoesSuperarem200Caracteres() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 1L, LocalDate.of(2026, 1, 10), null, "O".repeat(201));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("observacoes")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando dataPrevisaoFim for anterior à dataInicio")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar400QuandoDataPrevisaoFimForAnteriorADataInicio() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 1L, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 1), null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("dataPrevisaoFim")));
    }

    @Test
    @DisplayName("Deve retornar 404 quando aluno não existir")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar404QuandoAlunoNaoExistir() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                9999L, 1L, LocalDate.of(2026, 1, 10), null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 404 quando plano não existir")
    @DataSet(value = "datasets/vinculo-aluno-treino-base.yml")
    void deveRetornar404QuandoPlanoNaoExistir() throws Exception {
        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 9999L, LocalDate.of(2026, 1, 10), null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
