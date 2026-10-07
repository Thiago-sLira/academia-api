package com.academia.api.dtos.responses;

import com.academia.api.models.entities.TipoTreino;

public record TipoTreinoResponseDTO(
        Integer id,
        String nome,
        String descricao
) {
    public TipoTreinoResponseDTO(TipoTreino tipoTreino) {
        this(
                tipoTreino.getId(),
                tipoTreino.getNome(),
                tipoTreino.getDescricao()
        );
    }
}
