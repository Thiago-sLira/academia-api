package com.academia.api.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email é obrigatório") @Email String email,
        @NotBlank(message = "Senha é obrigatória") String senha
) {
}
