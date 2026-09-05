package com.academia.api.exceptions;

public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("Email e/ou senha inválidos");
    }
}
