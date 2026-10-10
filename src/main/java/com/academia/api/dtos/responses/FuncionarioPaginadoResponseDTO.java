package com.academia.api.dtos.responses;

import java.util.List;

public record FuncionarioPaginadoResponseDTO(
        List<FuncionarioResponseDTO> funcionarios,
        long totalRegistros
) {
}
