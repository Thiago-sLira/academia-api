package com.academia.api.exceptions;

import com.academia.api.exceptions.dtos.CampoErroDTO;
import com.academia.api.exceptions.dtos.ErroRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<CampoErroDTO> detalhes = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String defaultMessage = fieldError.getDefaultMessage();
            String campo = fieldError.getField();

            if (defaultMessage != null && defaultMessage.startsWith("Valores aceitos:")) {
                detalhes.add(new CampoErroDTO(
                        campo,
                        String.format("Valor inválido para o campo '%s'", campo),
                        defaultMessage
                ));
            } else {
                detalhes.add(new CampoErroDTO(campo, defaultMessage));
            }
        }

        ErroRespostaDTO resposta = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos informados",
                detalhes
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroRespostaDTO> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErroRespostaDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> handleAlunoNaoEncontrado(AlunoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroRespostaDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
}
