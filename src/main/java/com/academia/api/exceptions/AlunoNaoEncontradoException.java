package com.academia.api.exceptions;

public class AlunoNaoEncontradoException extends RuntimeException {

    public AlunoNaoEncontradoException(Long id) {
        super("Aluno não encontrado com id: " + id);
    }
}
