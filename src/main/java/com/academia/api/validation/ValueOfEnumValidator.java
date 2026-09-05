package com.academia.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

    private List<String> acceptedValues;
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValueOfEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.toString().trim().isEmpty()) {
            return true;
        }

        String normalized = EnumNormalizer.normalize(value.toString());
        boolean valid = acceptedValues.contains(normalized);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            String userHelp = "Valores aceitos: " + acceptedValues;
            context.buildConstraintViolationWithTemplate(userHelp)
                    .addConstraintViolation();
        }

        return valid;
    }
}
