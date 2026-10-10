package com.academia.api.dtos.requests;

import com.academia.api.common.FiltroPaginado;

public record PlanoTreinoFiltroDTO(
        Long idTipoTreino,
        Long idProfessorCriador,
        String nivelRecomendado,
        Integer paginaAtual,
        Integer tamanhoPagina
) implements FiltroPaginado {
}
