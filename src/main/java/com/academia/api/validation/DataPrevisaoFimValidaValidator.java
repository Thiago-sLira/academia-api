package com.academia.api.validation;

import com.academia.api.dtos.requests.VinculoAlunoTreinoRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataPrevisaoFimValidaValidator implements ConstraintValidator<DataPrevisaoFimValida, VinculoAlunoTreinoRequestDTO> {

    private String message;

    @Override
    public void initialize(DataPrevisaoFimValida annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(VinculoAlunoTreinoRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.dataPrevisaoFim() == null) {
            return true;
        }
        if (dto.dataInicio() == null) {
            return true;
        }
        boolean valid = !dto.dataPrevisaoFim().isBefore(dto.dataInicio());
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("dataPrevisaoFim")
                    .addConstraintViolation();
        }
        return valid;
    }
}
