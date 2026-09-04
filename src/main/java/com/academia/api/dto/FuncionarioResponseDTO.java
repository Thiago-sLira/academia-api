package com.academia.api.dto;

import com.academia.api.model.Funcionario;
import com.academia.api.model.PerfilFuncionario;

import java.time.LocalDateTime;

public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String email,
        String registroAcademico,
        PerfilFuncionario perfil,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getRegistroAcademico(),
                funcionario.getPerfil(),
                funcionario.getAtivo(),
                funcionario.getCriadoEm()
        );
    }
}
