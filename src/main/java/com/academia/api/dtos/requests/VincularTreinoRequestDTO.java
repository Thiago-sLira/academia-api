package com.academia.api.dtos.requests;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record VincularTreinoRequestDTO(
    @NotNull(message = "O ID do aluno é obrigatório")
    Long alunoId,

    @NotNull(message = "O ID do plano de treino é obrigatório")
    Long planoTreinoId,

    @NotNull(message = "A data de início é obrigatória")
    LocalDate dataInicio
) {}

