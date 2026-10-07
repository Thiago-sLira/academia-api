package com.academia.api.exceptions;

public class PerfilNaoAutorizadoException extends RuntimeException {

    public PerfilNaoAutorizadoException() {
        super("Apenas funcionários com perfil PROFESSOR podem criar planos de treino");
    }
}
