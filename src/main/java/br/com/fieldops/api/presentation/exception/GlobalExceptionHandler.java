package br.com.fieldops.api.presentation.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    // Trata falha de credenciais inválidas (e-mail/senha incorretos) -> HTTP 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> tratarBadCredentials() {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        resposta.put("erro", "Não Autorizado");
        resposta.put("mensagem", "Usuário ou senha inválidos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    // Trata tentativa de login com usuário inativado (ativo = false) -> HTTP 401
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> tratarDisabledException() {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        resposta.put("erro", "Acesso Negado");
        resposta.put("mensagem", "Usuário inativado no sistema.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    // Trata outras falhas de autenticação genéricas do Spring Security -> HTTP 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> tratarAuthenticationException(AuthenticationException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        resposta.put("erro", "Não Autorizado");
        resposta.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    // Trata regras de negócio e validações manuais disparadas pelos Services
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("erro", "Requisição Inválida");
        resposta.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // Trata erros de validação do Spring (@Valid / @NotNull / @NotBlank) -> HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarErro400(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(erros);
    }

    // Trata erros inesperados do servidor -> HTTP 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErro500(Exception ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        resposta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        resposta.put("erro", "Erro interno no servidor");
        resposta.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }
}