package com.academia.api.dtos.requests;

import com.academia.api.models.enums.CategoriaEquipamento;
import com.academia.api.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipamentoRequestDTO(
        @NotBlank(message = "O campo 'nome' é obrigatório")
        @Size(max = 100, message = "O campo 'nome' deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O campo 'categoria' é obrigatório")
        @ValueOfEnum(enumClass = CategoriaEquipamento.class, message = "Valor inválido para o campo 'categoria'")
        String categoria,

        @Size(max = 255, message = "O campo 'descricao' deve ter no máximo 255 caracteres")
        String descricao,

        Boolean ativo
) {
}
