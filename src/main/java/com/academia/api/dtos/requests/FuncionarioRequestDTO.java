package com.academia.api.dtos.requests;

import com.academia.api.models.enums.PerfilFuncionario;
import com.academia.api.validation.ValueOfEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FuncionarioRequestDTO(
        @NotBlank(message = "O campo 'nome' é obrigatório")
        @Size(max = 100, message = "O campo 'nome' deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O campo 'email' é obrigatório")
        @Email(message = "O campo 'email' deve ser um e-mail válido")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "O campo 'senha' é obrigatório")
        String senha,

        String registroAcademico,

        @ValueOfEnum(enumClass = PerfilFuncionario.class, message = "Valor inválido para o campo 'perfil'")
        String perfil
) {
}
