package com.academia.api.aluno;

import com.academia.api.BaseIntegrationTest;
import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastrarAlunoIntegrationTest extends BaseIntegrationTest {

    private static final String URL_ALUNOS = "/api/alunos";

    @Test
    @DisplayName("Deve cadastrar um aluno com sucesso quando todos os campos forem informados e válidos, inclusive enum com normalização de texto")
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
                "masculino", // string normalizada para MASCULINO
                "intermediario", // string normalizada para INTERMEDIARIO
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
    @DisplayName("Deve cadastrar um aluno com sucesso quando apenas campos obrigatórios forem informados (nome, email, telefone)")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/aluno-post-minimo-esperado.yml", ignoreCols = {"id_aluno", "criado_em", "atualizado_em"})
    void deveCadastrarAlunoComSucessoQuandoApenasCamposObrigatorios() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Maria Souza",
                "maria.souza@email.com",
                "11999998888",
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
                .andExpect(jsonPath("$.telefone").value("11999998888"))
                .andExpect(jsonPath("$.idade").doesNotExist())
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando nome estiver em branco com mensagem 'O campo 'nome' é obrigatório'")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoNomeEstiverEmBranco() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "",
                "teste.nome@email.com",
                "11999998888",
                null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'nome' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando email for inválido")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                "email-invalido",
                "11999998888",
                null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("email")));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando telefone estiver em branco com mensagem 'O campo 'telefone' é obrigatório'")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400QuandoTelefoneEstiverEmBranco() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                "carlos@email.com",
                "",
                null, null, null, null, null, null, null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("O campo 'telefone' é obrigatório")));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request com userHelp quando valor de enum for inválido")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400ComUserHelpQuandoEnumInvalido() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                "carlos@email.com",
                "11999998888",
                25,
                new BigDecimal("80.00"),
                new BigDecimal("1.80"),
                "TIPO_DESCONHECIDO",
                "INICIANTE",
                3,
                null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("genero")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem("Valor inválido para o campo 'genero'")))
                .andExpect(jsonPath("$.detalhes[0].userHelp", containsString("Valores aceitos: [MASCULINO, FEMININO, OUTRO, NAO_INFORMADO]")));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request com instruções de formato quando peso possuir dígitos inválidos")
    @DataSet(value = "datasets/alunos-vazio.yml")
    @ExpectedDataSet(value = "datasets/alunos-vazio.yml")
    void deveRetornar400ComInstrucoesQuandoPesoInvalido() throws Exception {
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Carlos Alberto",
                "carlos@email.com",
                "11999998888",
                25,
                new BigDecimal("1234.567"), // mais que 3 inteiros e 2 casas
                new BigDecimal("1.80"),
                "MASCULINO",
                "INICIANTE",
                3,
                null
        );

        mockMvc.perform(post(URL_ALUNOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[*].campo", hasItem("peso")))
                .andExpect(jsonPath("$.detalhes[*].mensagem", hasItem(containsString("O campo 'peso' deve ser informado com até 3 dígitos inteiros e 2 casas decimais"))));
    }
}
