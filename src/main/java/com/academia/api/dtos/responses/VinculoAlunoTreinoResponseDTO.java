package com.academia.api.dtos.responses;

import com.academia.api.models.entities.AlunoTreino;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VinculoAlunoTreinoResponseDTO(
        Long idVinculo,
        Long idAluno,
        Long idPlano,
        LocalDate dataInicio,
        LocalDate dataPrevisaoFim,
        String status,
        String observacoes,
        LocalDateTime criadoEm
) {
    public VinculoAlunoTreinoResponseDTO(AlunoTreino entity) {
        this(
                entity.getIdVinculo(),
                entity.getAluno().getId(),
                entity.getPlanoTreino().getId(),
                entity.getDataInicio(),
                entity.getDataPrevisaoFim(),
                entity.getStatus().name(),
                entity.getObservacoes(),
                entity.getCriadoEm()
        );
    }
}
