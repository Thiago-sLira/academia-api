package com.academia.api.dtos.responses;

import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.models.enums.NivelExperiencia;

import java.time.LocalDateTime;
import java.util.List;

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
        String nomeProfessor,
        List<AlunoResumoDTO> alunosVinculados
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
                plano.getProfessorCriador().getNome(),
                List.of()
        );
    }

    public PlanoTreinoListagemResponseDTO(PlanoTreino plano, List<AlunoResumoDTO> alunosVinculados) {
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
                plano.getProfessorCriador().getNome(),
                alunosVinculados
        );
    }
}
