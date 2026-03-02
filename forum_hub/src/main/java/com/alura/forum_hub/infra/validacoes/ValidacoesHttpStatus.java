package com.alura.forum_hub.infra.validacoes;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidacoesHttpStatus {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity statusError400(MethodArgumentNotValidException methodArgumentNotValidException) {
    var erros = methodArgumentNotValidException.getFieldErrors();
    return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
  }

  private record DadosErroValidacao(String campo, String mensagem) {

    public DadosErroValidacao(FieldError erro) {
      this(erro.getField(), erro.getDefaultMessage());
    }
  }
}
