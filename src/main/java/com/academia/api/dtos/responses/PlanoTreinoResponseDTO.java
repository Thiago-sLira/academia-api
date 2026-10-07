package com.academia.api.dtos.responses;

import com.academia.api.models.entities.PlanoTreino;

public record PlanoTreinoResponseDTO(
        Long idPlanoTreino
) {
    public PlanoTreinoResponseDTO(PlanoTreino planoTreino) {
        this(planoTreino.getId());
    }
}
