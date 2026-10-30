package com.academia.api.vinculo;

import com.academia.api.dtos.requests.VinculoAlunoTreinoRequestDTO;
import com.academia.api.dtos.responses.VinculoAlunoTreinoResponseDTO;
import com.academia.api.exceptions.AlunoComTreinoAtivoException;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.exceptions.PlanoTreinoNaoEncontradoException;
import com.academia.api.models.entities.Aluno;
import com.academia.api.models.entities.AlunoTreino;
import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.models.enums.StatusVinculo;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.repositories.AlunoTreinoRepository;
import com.academia.api.repositories.PlanoTreinoRepository;
import com.academia.api.services.AlunoTreinoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - AlunoTreinoService")
class AlunoTreinoServiceTest {

    @Mock
    private AlunoTreinoRepository alunoTreinoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PlanoTreinoRepository planoTreinoRepository;

    @InjectMocks
    private AlunoTreinoService service;

    private VinculoAlunoTreinoRequestDTO dtoValido() {
        return new VinculoAlunoTreinoRequestDTO(1L, 1L, LocalDate.of(2026, 1, 10), null, null);
    }

    @Test
    @DisplayName("Deve salvar vínculo com status ATIVO quando dados são válidos")
    void vincular_comDadosValidos_deveSalvarComStatusAtivo() {
        Aluno aluno = Aluno.builder().build();
        PlanoTreino plano = PlanoTreino.builder().build();

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(planoTreinoRepository.findById(1L)).thenReturn(Optional.of(plano));

        ArgumentCaptor<AlunoTreino> captor = ArgumentCaptor.forClass(AlunoTreino.class);
        AlunoTreino saved = AlunoTreino.builder()
                .idVinculo(10L)
                .aluno(aluno)
                .planoTreino(plano)
                .dataInicio(LocalDate.of(2026, 1, 10))
                .status(StatusVinculo.ATIVO)
                .build();
        when(alunoTreinoRepository.save(any())).thenReturn(saved);

        VinculoAlunoTreinoResponseDTO result = service.vincular(dtoValido());

        verify(alunoTreinoRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(StatusVinculo.ATIVO);
        assertThat(result.idVinculo()).isEqualTo(10L);
        assertThat(result.status()).isEqualTo("ATIVO");
    }

    @Test
    @DisplayName("Deve lançar AlunoNaoEncontradoException quando aluno não existir")
    void vincular_comAlunoInexistente_deveLancarAlunoNaoEncontradoException() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                99L, 1L, LocalDate.of(2026, 1, 10), null, null);

        assertThrows(AlunoNaoEncontradoException.class, () -> service.vincular(dto));
    }

    @Test
    @DisplayName("Deve lançar PlanoTreinoNaoEncontradoException quando plano não existir")
    void vincular_comPlanoInexistente_deveLancarPlanoTreinoNaoEncontradoException() {
        Aluno aluno = Aluno.builder().build();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(planoTreinoRepository.findById(99L)).thenReturn(Optional.empty());

        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 99L, LocalDate.of(2026, 1, 10), null, null);

        assertThrows(PlanoTreinoNaoEncontradoException.class, () -> service.vincular(dto));
    }

    @Test
    @DisplayName("Deve lançar AlunoComTreinoAtivoException e não salvar quando aluno já tem vínculo ativo")
    void vincular_comAlunoJaComVinculoAtivo_deveLancarAlunoComTreinoAtivoException() {
        Aluno aluno = Aluno.builder().build();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoTreinoRepository.existsByAlunoIdAndStatus(1L, StatusVinculo.ATIVO)).thenReturn(true);

        VinculoAlunoTreinoRequestDTO dto = new VinculoAlunoTreinoRequestDTO(
                1L, 1L, LocalDate.of(2026, 1, 10), null, null);

        assertThrows(AlunoComTreinoAtivoException.class, () -> service.vincular(dto));
        verify(alunoTreinoRepository, never()).save(any());
    }
}
