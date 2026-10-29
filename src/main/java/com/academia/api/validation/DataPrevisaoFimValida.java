package com.academia.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DataPrevisaoFimValidaValidator.class)
public @interface DataPrevisaoFimValida {

    String message() default "A data de previsão de fim deve ser igual ou posterior à data de início";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
