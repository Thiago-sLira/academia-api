package com.academia.api.dtos.responses;

import com.academia.api.models.entities.Equipamento;
import com.academia.api.models.enums.CategoriaEquipamento;

public record EquipamentoResumoDTO(
        Integer id,
        String nome,
        CategoriaEquipamento categoria,
        String descricao
) {
    public EquipamentoResumoDTO(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getCategoria(),
                equipamento.getDescricao()
        );
    }
}
