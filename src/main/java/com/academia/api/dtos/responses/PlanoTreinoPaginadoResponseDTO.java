package com.academia.api.dtos.responses;

import java.util.List;

public record PlanoTreinoPaginadoResponseDTO(
        List<PlanoTreinoListagemResponseDTO> planos,
        long totalRegistros
) {
}
