package com.academia.api.dtos.responses;

import com.academia.api.models.enums.PerfilFuncionario;

public record LoginResponseDTO(
        String token,
        String tipo,
        Long id,
        String nome,
        String email,
        PerfilFuncionario perfil
) {
    public LoginResponseDTO(String token, Long id, String nome, String email, PerfilFuncionario perfil) {
        this(token, "Bearer", id, nome, email, perfil);
    }
}
