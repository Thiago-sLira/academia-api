package com.academia.api.dto.requests;

import com.academia.api.models.enums.PerfilFuncionario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FuncionarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Email é obrigatório") @Email String email,
        @NotBlank(message = "Senha é obrigatória") String senha,
        String registroAcademico,
        PerfilFuncionario perfil
) {
}
