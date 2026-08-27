package com.academia.api.dto;

import com.academia.api.model.Aluno;
import com.academia.api.model.Genero;
import com.academia.api.model.NivelExperiencia;

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