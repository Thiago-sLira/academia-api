package com.academia.api.exceptions;

public class EquipamentoNaoEncontradoException extends RuntimeException {

    public EquipamentoNaoEncontradoException(Integer id) {
        super("Equipamento não encontrado com id: " + id);
    }

    public EquipamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
