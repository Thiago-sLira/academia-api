package com.academia.api.models.enums;

import com.academia.api.validation.EnumNormalizer;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum CategoriaEquipamento {
    SUPERIORES,
    INFERIORES,
    CARDIO,
    PESO_LIVRE,
    ACESSORIO;

    @JsonCreator
    public static CategoriaEquipamento fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return EnumNormalizer.parseEnum(CategoriaEquipamento.class, value)
                .orElseThrow(() -> new IllegalArgumentException("Valor inválido para a categoria: " + value));
    }
}
