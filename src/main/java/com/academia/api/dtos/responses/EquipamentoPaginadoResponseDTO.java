package com.academia.api.dtos.responses;

import java.util.List;

public record EquipamentoPaginadoResponseDTO(
        List<EquipamentoResponseDTO> equipamentos,
        long totalRegistros
) {
}
