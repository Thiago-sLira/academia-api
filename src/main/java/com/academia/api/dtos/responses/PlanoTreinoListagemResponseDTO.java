package com.academia.api.dtos.responses;

import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.models.enums.NivelExperiencia;

import java.time.LocalDateTime;

public record PlanoTreinoListagemResponseDTO(
        Long idPlanoTreino,
        String titulo,
        String descricao,
        NivelExperiencia nivelRecomendado,
        Boolean ativo,
        LocalDateTime criadoEm,
        Integer idTipoTreino,
        String nomeTipoTreino,
        Long idProfessorCriador,
        String nomeProfessor
) {
    public PlanoTreinoListagemResponseDTO(PlanoTreino plano) {
        this(
                plano.getId(),
                plano.getTitulo(),
                plano.getDescricao(),
                plano.getNivelRecomendado(),
                plano.getAtivo(),
                plano.getCriadoEm(),
                plano.getTipoTreino().getId(),
                plano.getTipoTreino().getNome(),
                plano.getProfessorCriador().getId(),
                plano.getProfessorCriador().getNome()
        );
    }
}
