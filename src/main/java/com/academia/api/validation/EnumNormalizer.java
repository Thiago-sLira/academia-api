package com.academia.api.validation;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Optional;

public final class EnumNormalizer {

    private EnumNormalizer() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String semAcentos = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return semAcentos
                .trim()
                .replaceAll("[\\s-]+", "_")
                .toUpperCase();
    }

    public static <E extends Enum<E>> Optional<E> parseEnum(Class<E> enumClass, String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }
        String normalized = normalize(value);
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equals(normalized))
                .findFirst();
    }
}
