package com.academia.api.dtos.requests;

import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.validation.ValueOfEnum;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AlunoRequestDTO(
        @NotBlank(message = "O campo 'nome' é obrigatório")
        @Size(max = 100, message = "O campo 'nome' deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O campo 'email' é obrigatório")
        @Email(message = "O campo 'email' deve ser um e-mail válido")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "O campo 'telefone' é obrigatório")
        @Size(max = 20, message = "O campo 'telefone' deve ter no máximo 20 caracteres")
        String telefone,

        @Min(value = 12, message = "O campo 'idade' deve ser no mínimo 12 anos")
        @Max(value = 120, message = "O campo 'idade' deve ser no máximo 120 anos")
        Integer idade,

        @DecimalMin(value = "20.00", message = "O campo 'peso' deve ser no mínimo 20.00 kg")
        @DecimalMax(value = "500.00", message = "O campo 'peso' deve ser no máximo 500.00 kg")
        @Digits(integer = 3, fraction = 2, message = "O campo 'peso' deve ser informado com até 3 dígitos inteiros e 2 casas decimais (ex: 75.50)")
        BigDecimal peso,

        @DecimalMin(value = "0.50", message = "O campo 'altura' deve ser no mínimo 0.50 m")
        @DecimalMax(value = "2.50", message = "O campo 'altura' deve ser no máximo 2.50 m")
        @Digits(integer = 1, fraction = 2, message = "O campo 'altura' deve ser informado com 1 dígito inteiro e 2 casas decimais (ex: 1.75)")
        BigDecimal altura,

        @ValueOfEnum(enumClass = Genero.class, message = "Valor inválido para o campo 'genero'")
        String genero,

        @ValueOfEnum(enumClass = NivelExperiencia.class, message = "Valor inválido para o campo 'nivelExperiencia'")
        String nivelExperiencia,

        @Min(value = 1, message = "O campo 'diasDisponiveisSemana' deve ser no mínimo 1 dia")
        @Max(value = 7, message = "O campo 'diasDisponiveisSemana' deve ser no máximo 7 dias")
        Integer diasDisponiveisSemana,

        String restricaoMedica
) {
}
