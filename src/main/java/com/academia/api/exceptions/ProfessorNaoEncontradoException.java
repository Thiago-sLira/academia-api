package com.academia.api.exceptions;

public class ProfessorNaoEncontradoException extends RuntimeException {

    public ProfessorNaoEncontradoException(Long id) {
        super("Professor não encontrado com id: " + id);
    }
}
