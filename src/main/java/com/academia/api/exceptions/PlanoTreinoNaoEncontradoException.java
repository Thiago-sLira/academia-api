package com.academia.api.exceptions;

public class PlanoTreinoNaoEncontradoException extends RuntimeException {

    public PlanoTreinoNaoEncontradoException(Long id) {
        super("Plano de treino não encontrado com id: " + id);
    }
}
