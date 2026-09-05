package com.academia.api.funcionario;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.FuncionarioRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastrarFuncionarioIntegrationTest extends BaseIntegrationTest {

    private static final String URL_FUNCIONARIOS = "/api/funcionarios";

    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso quando todos os campos forem informados e válidos")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveCadastrarFuncionarioComSucessoQuandoPayloadCompleto() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Carlos Professor",
                "carlos@academia.com",
                "senha123",
                "PROF-001",
                "PROFESSOR"
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Carlos Professor"))
                .andExpect(jsonPath("$.email").value("carlos@academia.com"))
                .andExpect(jsonPath("$.registroAcademico").value("PROF-001"))
                .andExpect(jsonPath("$.perfil").value("PROFESSOR"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso quando apenas os campos obrigatórios forem informados")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    void deveCadastrarFuncionarioComSucessoQuandoApenasCamposObrigatorios() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Maria Admin",
                "maria@academia.com",
                "senha456",
                null,
                null
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Maria Admin"))
                .andExpect(jsonPath("$.email").value("maria@academia.com"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome estiver em branco")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoNomeEmBranco() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "", "teste@academia.com", "senha123", null, null
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o email for inválido")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Carlos Professor", "email-invalido", "senha123", null, null
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("email")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'email' deve ser um e-mail válido")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha estiver em branco")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoSenhaEmBranco() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Carlos Professor", "carlos@academia.com", "", null, null
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'senha' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 com userHelp quando o perfil informado for inválido")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400ComUserHelpQuandoPerfilInvalido() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Carlos Professor", "carlos@academia.com", "senha123", null, "DIRETOR"
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("perfil")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("Valor inválido para o campo 'perfil'")))
                .andExpect(jsonPath("$.detalhes[0].userHelp", containsString("Valores aceitos:")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o e-mail já estiver cadastrado")
    @DataSet(value = "datasets/funcionario-existente.yml")
    @ExpectedDataSet(value = "datasets/funcionario-existente.yml", ignoreCols = {"criado_em"})
    void deveRetornar409QuandoEmailDuplicado() throws Exception {
        FuncionarioRequestDTO requestDTO = new FuncionarioRequestDTO(
                "Outro Nome",
                "admin@academia.com",  // e-mail já existente
                "senha123",
                null,
                null
        );

        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.erro").value("O e-mail informado já está em uso. Utilize um e-mail diferente."));
    }

    @Test
    @DisplayName("Deve retornar 415 quando o Content-Type não for application/json")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar415QuandoContentTypeInvalido() throws Exception {
        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("qualquer coisa"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.erro", containsStringIgnoringCase("application/json")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o corpo da requisição for JSON malformado")
    @DataSet(value = "datasets/funcionarios-vazio.yml")
    @ExpectedDataSet(value = "datasets/funcionarios-vazio.yml")
    void deveRetornar400QuandoCorpoJsonMalformado() throws Exception {
        mockMvc.perform(post(URL_FUNCIONARIOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalido }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro", containsStringIgnoringCase("JSON inválido")));
    }
}
