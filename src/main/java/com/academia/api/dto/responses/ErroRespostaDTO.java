package com.academia.api.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroRespostaDTO(
        String timestamp,
        Integer status,
        String erro,
        List<CampoErroDTO> detalhes
) {
    public ErroRespostaDTO(Integer status, String erro, List<CampoErroDTO> detalhes) {
        this(LocalDateTime.now().toString(), status, erro, detalhes);
    }

    public ErroRespostaDTO(Integer status, String erro) {
        this(LocalDateTime.now().toString(), status, erro, null);
    }
}
