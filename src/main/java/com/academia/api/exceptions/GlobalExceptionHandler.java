package com.academia.api.exceptions;

import com.academia.api.exceptions.dtos.CampoErroDTO;
import com.academia.api.exceptions.dtos.ErroRespostaDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroRespostaDTO> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String enviado = ex.getContentType() != null ? ex.getContentType().toString() : "não informado";
        String mensagem = String.format(
                "Content-Type '%s' não é suportado. Utilize 'application/json'.", enviado);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErroRespostaDTO(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(),
                        "O corpo da requisição está ausente ou contém JSON inválido. " +
                        "Verifique a sintaxe e reenvie."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroRespostaDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagem = String.format(
                "O parâmetro '%s' recebeu o valor '%s', que não é um número inteiro válido.",
                ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(), mensagem));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroRespostaDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroRespostaDTO(HttpStatus.CONFLICT.value(),
                        "O e-mail informado já está em uso. Utilize um e-mail diferente."));
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
