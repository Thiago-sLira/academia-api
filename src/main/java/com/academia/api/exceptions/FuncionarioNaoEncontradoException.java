package com.academia.api.exceptions;

public class FuncionarioNaoEncontradoException extends RuntimeException {

    public FuncionarioNaoEncontradoException(Long id) {
        super("Funcionário não encontrado com id: " + id);
    }
}
