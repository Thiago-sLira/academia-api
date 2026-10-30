package com.academia.api.aluno;

import com.academia.api.exceptions.AlunoComTreinoAtivoException;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.models.enums.StatusVinculo;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.repositories.AlunoTreinoRepository;
import com.academia.api.services.AlunoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - AlunoService")
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private AlunoTreinoRepository alunoTreinoRepository;

    @InjectMocks
    private AlunoService service;

    @Test
    @DisplayName("Deve lançar AlunoComTreinoAtivoException e não chamar deleteById quando aluno tem treino ativo")
    void deletar_alunoComTreinoAtivo_deveLancarExcecaoENaoChamarDeleteById() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(alunoTreinoRepository.existsByAlunoIdAndStatus(1L, StatusVinculo.ATIVO)).thenReturn(true);

        assertThrows(AlunoComTreinoAtivoException.class, () -> service.deletar(1L));
        verify(alunoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve chamar deleteById quando aluno não tem treino ativo")
    void deletar_alunoSemTreinoAtivo_deveChamarDeleteById() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(alunoTreinoRepository.existsByAlunoIdAndStatus(1L, StatusVinculo.ATIVO)).thenReturn(false);

        service.deletar(1L);

        verify(alunoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar AlunoNaoEncontradoException quando aluno não existir")
    void deletar_alunoInexistente_deveLancarAlunoNaoEncontradoException() {
        when(alunoRepository.existsById(999L)).thenReturn(false);

        assertThrows(AlunoNaoEncontradoException.class, () -> service.deletar(999L));
        verify(alunoRepository, never()).deleteById(any());
    }
}
