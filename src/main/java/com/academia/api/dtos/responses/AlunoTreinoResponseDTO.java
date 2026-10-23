package com.academia.api.dtos.responses;

import java.time.LocalDate;

public record AlunoTreinoResponseDTO(
    Long id,
    Long alunoId,
    String nomeAluno,
    Long planoTreinoId,
    String nomeTreino,
    LocalDate dataInicio,
    LocalDate dataFim,
    Boolean ativo
) {}
