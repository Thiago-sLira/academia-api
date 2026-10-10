package com.academia.api.dtos.requests;

import com.academia.api.common.FiltroPaginado;
import com.academia.api.models.enums.PerfilFuncionario;

public record FuncionarioFiltroDTO(
        Long id,
        String nome,
        String email,
        String registroAcademico,
        PerfilFuncionario perfil,
        Boolean ativo,
        Integer paginaAtual,
        Integer tamanhoPagina
) implements FiltroPaginado {
}
