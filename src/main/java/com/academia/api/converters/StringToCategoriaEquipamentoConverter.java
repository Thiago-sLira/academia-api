package com.academia.api.converters;

import com.academia.api.models.enums.CategoriaEquipamento;
import com.academia.api.validation.EnumNormalizer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoriaEquipamentoConverter implements Converter<String, CategoriaEquipamento> {

    @Override
    public CategoriaEquipamento convert(@NonNull String source) {
        if (source.isBlank()) {
            return null;
        }
        return EnumNormalizer.parseEnum(CategoriaEquipamento.class, source)
                .orElseThrow(() -> new IllegalArgumentException("Valor inválido para a categoria do equipamento: " + source));
    }
}
