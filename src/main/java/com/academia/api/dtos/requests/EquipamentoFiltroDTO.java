package com.academia.api.dtos.requests;

import com.academia.api.common.FiltroPaginado;
import com.academia.api.models.enums.CategoriaEquipamento;

public record EquipamentoFiltroDTO(
        Long id,
        String nome,
        CategoriaEquipamento categoria,
        Integer paginaAtual,
        Integer tamanhoPagina
) implements FiltroPaginado {
}
