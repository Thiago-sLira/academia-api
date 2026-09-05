package com.academia.api.controller;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dto.requests.AlunoRequestDTO;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlunoPostIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNOS = "/api/alunos";

    @Test
    @DisplayName("Deve cadastrar um aluno com sucesso quando todos os campos forem informados e válidos")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/aluno-post-completo-esperado.yml", ignoreCols = {"id_aluno", "criado_em", "atualizado_em"})
    void deveCadastrarAlunoComSucessoQuandoPayloadCompleto() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "João da Silva",
                "joao.silva@email.com",
                "11987654321",
                25,
                new BigDecimal("78.50"),
                new BigDecimal("1.80"),
                Genero.MASCULINO,
                NivelExperiencia.INTERMEDIARIO,
                4,
                "Nenhuma restrição"
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.telefone").value("11987654321"))
                .andExpect(jsonPath("$.idade").value(25))
                .andExpect(jsonPath("$.peso").value(78.50))
                .andExpect(jsonPath("$.altura").value(1.80))
                .andExpect(jsonPath("$.genero").value("MASCULINO"))
                .andExpect(jsonPath("$.nivelExperiencia").value("INTERMEDIARIO"))
                .andExpect(jsonPath("$.diasDisponiveisSemana").value(4))
                .andExpect(jsonPath("$.restricaoMedica").value("Nenhuma restrição"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve cadastrar um aluno com sucesso quando apenas campos obrigatórios forem informados")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/aluno-post-minimo-esperado.yml", ignoreCols = {"id_aluno", "criado_em", "atualizado_em"})
    void deveCadastrarAlunoComSucessoQuandoApenasCamposObrigatorios() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Maria Souza",
                "maria.souza@email.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Maria Souza"))
                .andExpect(jsonPath("$.email").value("maria.souza@email.com"))
                .andExpect(jsonPath("$.telefone").doesNotExist())
                .andExpect(jsonPath("$.idade").doesNotExist())
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando nome estiver em branco")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoNomeEstiverEmBranco() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "",
                "teste.nome@email.com",
                null, null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando email for inválido")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                "email-invalido",
                null, null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando email for nulo")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoEmailNulo() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                null,
                null, null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
