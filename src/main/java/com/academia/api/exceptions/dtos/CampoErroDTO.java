package com.academia.api.exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CampoErroDTO(
        String campo,
        String mensagem,
        String userHelp
) {
    public CampoErroDTO(String campo, String mensagem) {
        this(campo, mensagem, null);
    }
}
