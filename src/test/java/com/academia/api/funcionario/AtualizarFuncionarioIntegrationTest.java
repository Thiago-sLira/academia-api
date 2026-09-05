package com.academia.api.funcionario;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.FuncionarioRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AtualizarFuncionarioIntegrationTest extends BaseIntegrationTest {

    private static final String URL_FUNCIONARIO_POR_ID = "/api/funcionarios/{id}";

    @Test
    @DisplayName("Deve atualizar o funcionário com sucesso e persistir os novos dados no banco")
    @DataSet(value = "datasets/funcionario-existente.yml")
    @ExpectedDataSet(value = "datasets/funcionario-put-esperado.yml", ignoreCols = {"criado_em", "senha"})
    void deveAtualizarFuncionarioComSucesso() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Admin Atualizado",
                "admin.atualizado@academia.com",
                "novaSenha123",
                "ADM-999",
                "PROFESSOR"
        );

        mockMvc.perform(put(URL_FUNCIONARIO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Admin Atualizado"))
                .andExpect(jsonPath("$.email").value("admin.atualizado@academia.com"))
                .andExpect(jsonPath("$.registroAcademico").value("ADM-999"))
                .andExpect(jsonPath("$.perfil").value("PROFESSOR"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 com ErroRespostaDTO quando o ID não existir")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Qualquer Nome", "qualquer@academia.com", "senha123", null, null
        );

        mockMvc.perform(put(URL_FUNCIONARIO_POR_ID, 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Funcionário não encontrado com id: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 e não alterar o banco quando o nome estiver em branco")
    @DataSet(value = "datasets/funcionario-existente.yml")
    @ExpectedDataSet(value = "datasets/funcionario-existente.yml", ignoreCols = {"criado_em"})
    void deveRetornar400ENaoAlterarBancoQuandoNomeEmBranco() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "", "admin@academia.com", "senha123", null, null
        );

        mockMvc.perform(put(URL_FUNCIONARIO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o valor do campo perfil for inválido")
    @DataSet(value = "datasets/funcionario-existente.yml")
    @ExpectedDataSet(value = "datasets/funcionario-existente.yml", ignoreCols = {"criado_em"})
    void deveRetornar400QuandoPerfilInvalido() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Admin Teste", "admin@academia.com", "senha123", null, "DIRETOR"
        );

        mockMvc.perform(put(URL_FUNCIONARIO_POR_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("perfil")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("Valor inválido para o campo 'perfil'")));
    }

    @Test
    @DisplayName("Deve retornar 400 com ErroRespostaDTO quando o ID informado na URL não for um número")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoIdNaoForNumerico() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Qualquer Nome", "qualquer@academia.com", "senha123", null, null
        );

        mockMvc.perform(put("/api/funcionarios/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsString("'id'")))
                .andExpect(jsonPath("$.erro", containsString("'abc'")))
                .andExpect(jsonPath("$.erro", containsString("número inteiro")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o e-mail já pertencer a outro funcionário")
    @DataSet(value = "datasets/funcionario-existente.yml")
    @ExpectedDataSet(value = "datasets/funcionario-existente.yml", ignoreCols = {"criado_em"})
    void deveRetornar409QuandoEmailJaEmUso() throws Exception {
        // Tenta atualizar funcionário 2 com o e-mail do funcionário 1
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Professor Silva",
                "admin@academia.com",  // e-mail já pertence ao funcionário 1
                "senha123",
                null,
                "PROFESSOR"
        );

        mockMvc.perform(put("/api/funcionarios/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.erro").value("O e-mail informado já está em uso. Utilize um e-mail diferente."));
    }
}
