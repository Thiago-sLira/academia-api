package com.academia.api.dtos.requests;

import com.academia.api.validation.DataPrevisaoFimValida;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@DataPrevisaoFimValida
public record VinculoAlunoTreinoRequestDTO(

        @NotNull(message = "O ID do aluno é obrigatório")
        @Positive(message = "O ID do aluno deve ser positivo")
        Long idAluno,

        @NotNull(message = "O ID do plano é obrigatório")
        @Positive(message = "O ID do plano deve ser positivo")
        Long idPlano,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,

        LocalDate dataPrevisaoFim,

        @Size(max = 200, message = "Observações não podem ultrapassar 200 caracteres")
        String observacoes
) {
}
