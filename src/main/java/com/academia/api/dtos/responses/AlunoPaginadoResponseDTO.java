package com.academia.api.dtos.responses;

import java.util.List;

public record AlunoPaginadoResponseDTO(
        List<AlunoResponseDTO> alunos,
        long totalRegistros
) {
}
