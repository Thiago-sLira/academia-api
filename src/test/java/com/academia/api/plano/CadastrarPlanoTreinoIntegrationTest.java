package com.academia.api.plano;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.PlanoTreinoRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testes de Integração - Cadastrar Plano de Treino")
class CadastrarPlanoTreinoIntegrationTest extends BaseIntegrationTest {

    private static final String URL = "/api/planos-treino";

    private PlanoTreinoRequestDTO requestValido() {
        return new PlanoTreinoRequestDTO(
                1L,
                10L,
                "Plano de Hipertrofia",
                "Plano focado em ganho de massa muscular para nível intermediário.",
                "INTERMEDIARIO"
        );
    }

    @Test
    @DisplayName("Deve cadastrar um plano de treino com sucesso e retornar 201 com idPlanoTreino")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveCadastrarPlanoComSucesso() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPlanoTreino").isNumber());
    }

    @Test
    @DisplayName("Deve retornar 400 quando idTipoTreino for nulo")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoIdTipoTreinoNulo() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                null, 10L, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("idTipoTreino")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'idTipoTreino' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando idProfessorCriador for nulo")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoIdProfessorCriadorNulo() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, null, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("idProfessorCriador")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'idProfessorCriador' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando titulo estiver em branco")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoTituloEmBranco() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, "", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("titulo")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'titulo' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando titulo ultrapassar 100 caracteres")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoTituloAcimaDe100Chars() throws Exception {
        String titulo101 = "A".repeat(101);
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, titulo101, "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("titulo")))
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'titulo' deve ter no máximo 100 caracteres")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando descricao estiver em branco")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoDescricaoEmBranco() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, "Titulo válido", "", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("descricao")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'descricao' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando descricao ultrapassar 250 caracteres")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoDescricaoAcimaDe250Chars() throws Exception {
        String descricao251 = "D".repeat(251);
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, "Titulo válido", descricao251, "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("descricao")))
                .andExpect(jsonPath("$.detalhes[*].mensagem",
                        hasItem("O campo 'descricao' deve ter no máximo 250 caracteres")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando nivelRecomendado estiver em branco")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoNivelRecomendadoEmBranco() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, "Titulo válido", "Descrição válida do plano de treino.", ""
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("nivelRecomendado")));
    }

    @Test
    @DisplayName("Deve retornar 400 com userHelp quando nivelRecomendado for inválido")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400ComUserHelpQuandoNivelRecomendadoInvalido() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 10L, "Titulo válido", "Descrição válida do plano de treino.", "EXPERT"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("nivelRecomendado")))
                .andExpect(jsonPath("$.detalhes[0].userHelp", containsString("Valores aceitos:")));
    }

    @Test
    @DisplayName("Deve retornar 404 quando idProfessorCriador não existir")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar404QuandoProfessorNaoExistir() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 999L, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Professor não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 403 quando o funcionário tiver perfil ADMIN")
    @DataSet(value = "datasets/plano-treino-existente.yml")
    void deveRetornar403QuandoPerfilAdmin() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 11L, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.erro").value(
                        "Apenas funcionários com perfil PROFESSOR podem criar planos de treino"));
    }

    @Test
    @DisplayName("Deve retornar 403 quando o funcionário tiver perfil ALUNO")
    @DataSet(value = "datasets/plano-treino-existente.yml")
    void deveRetornar403QuandoPerfilAluno() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                1L, 12L, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.erro").value(
                        "Apenas funcionários com perfil PROFESSOR podem criar planos de treino"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando idTipoTreino não existir")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    @ExpectedDataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar404QuandoTipoTreinoNaoExistir() throws Exception {
        PlanoTreinoRequestDTO dto = new PlanoTreinoRequestDTO(
                999L, 10L, "Titulo válido", "Descrição válida do plano de treino.", "INICIANTE"
        );

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Tipo de treino não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 415 quando o Content-Type não for application/json")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar415QuandoContentTypeInvalido() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("qualquer coisa"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o corpo da requisição for JSON malformado")
    @DataSet(value = "datasets/planos-treino-vazio.yml")
    void deveRetornar400QuandoCorpoJsonMalformado() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalido }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
