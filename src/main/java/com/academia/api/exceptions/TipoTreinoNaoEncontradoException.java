package com.academia.api.exceptions;

public class TipoTreinoNaoEncontradoException extends RuntimeException {

    public TipoTreinoNaoEncontradoException(Long id) {
        super("Tipo de treino não encontrado com id: " + id);
    }
}
