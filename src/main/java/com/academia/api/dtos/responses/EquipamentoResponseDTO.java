package com.academia.api.dtos.responses;

import com.academia.api.models.entities.Equipamento;
import com.academia.api.models.enums.CategoriaEquipamento;

import java.time.LocalDateTime;

public record EquipamentoResponseDTO(
        Integer id,
        String nome,
        CategoriaEquipamento categoria,
        String descricao,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public EquipamentoResponseDTO(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getCategoria(),
                equipamento.getDescricao(),
                equipamento.getAtivo(),
                equipamento.getCriadoEm()
        );
    }
}
