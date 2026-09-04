package com.academia.api.exception;

public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("Email e/ou senha inválidos");
    }
}
