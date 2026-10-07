package com.academia.api.dtos.requests;

import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlanoTreinoRequestDTO(

        @NotNull(message = "O campo 'idTipoTreino' é obrigatório")
        Long idTipoTreino,

        @NotNull(message = "O campo 'idProfessorCriador' é obrigatório")
        Long idProfessorCriador,

        @NotBlank(message = "O campo 'titulo' é obrigatório")
        @Size(max = 100, message = "O campo 'titulo' deve ter no máximo 100 caracteres")
        String titulo,

        @NotBlank(message = "O campo 'descricao' é obrigatório")
        @Size(max = 250, message = "O campo 'descricao' deve ter no máximo 250 caracteres")
        String descricao,

        @NotBlank(message = "O campo 'nivelRecomendado' é obrigatório")
        @ValueOfEnum(enumClass = NivelExperiencia.class)
        String nivelRecomendado

) {
}
