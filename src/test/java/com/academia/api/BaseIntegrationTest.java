package com.academia.api;

import com.academia.api.repositories.AlunoRepository;
import com.academia.api.repositories.FuncionarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DBRider
@DBUnit(caseInsensitiveStrategy = com.github.database.rider.core.api.configuration.Orthography.LOWERCASE, disableSequenceFiltering = true)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AlunoRepository alunoRepository;

    @Autowired
    protected FuncionarioRepository funcionarioRepository;

    @org.junit.jupiter.api.BeforeEach
    void cleanState() {
        alunoRepository.deleteAll();
        funcionarioRepository.deleteAll();
    }
}
