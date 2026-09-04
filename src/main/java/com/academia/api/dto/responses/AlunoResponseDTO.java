package com.academia.api.dto.responses;

import com.academia.api.models.entities.Aluno;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;

import java.math.BigDecimal;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        Integer idade,
        BigDecimal peso,
        BigDecimal altura,
        Genero genero,
        NivelExperiencia nivelExperiencia,
        Integer diasDisponiveisSemana,
        String restricaoMedica,
        Boolean ativo
) {
    public AlunoResponseDTO(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getIdade(),
                aluno.getPeso(),
                aluno.getAltura(),
                aluno.getGenero(),
                aluno.getNivelExperiencia(),
                aluno.getDiasDisponiveisSemana(),
                aluno.getRestricaoMedica(),
                aluno.getAtivo()
        );
    }
}