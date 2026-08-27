package com.academia.api.dto;

import com.academia.api.model.Genero;
import com.academia.api.model.NivelExperiencia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AlunoRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Email é obrigatório") @Email String email,
        String telefone,
        Integer idade,
        BigDecimal peso,
        BigDecimal altura,
        Genero genero,
        NivelExperiencia nivelExperiencia,
        Integer diasDisponiveisSemana,
        String restricaoMedica
) {
}