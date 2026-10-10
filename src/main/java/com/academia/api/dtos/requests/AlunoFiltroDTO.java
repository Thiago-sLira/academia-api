package com.academia.api.dtos.requests;

import com.academia.api.common.FiltroPaginado;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;

import java.math.BigDecimal;

public record AlunoFiltroDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        Integer idade,
        BigDecimal peso,
        BigDecimal altura,
        Genero genero,
        NivelExperiencia nivelExperiencia,
        Integer paginaAtual,
        Integer tamanhoPagina
) implements FiltroPaginado {
}
