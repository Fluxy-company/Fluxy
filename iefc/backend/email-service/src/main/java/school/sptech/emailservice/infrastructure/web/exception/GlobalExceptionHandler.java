package school.sptech.emailservice.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import school.sptech.emailservice.domain.exception.EmailNaoEncontradoException;
import school.sptech.emailservice.domain.exception.EnvioEmailException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNaoEncontradoException.class)
    public ResponseEntity<ExceptionResponse> handleEmailNaoEncontrado(EmailNaoEncontradoException ex,
                                                                       WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "E-mail Nao Encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(IllegalStateException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Estado Invalido",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Argumento Invalido",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EnvioEmailException.class)
    public ResponseEntity<ExceptionResponse> handleEnvioEmail(EnvioEmailException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Falha no Envio do E-mail",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                             WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de Validacao");

        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage()));

        response.put("erros", erros);
        response.put("mensagem", "Um ou mais campos invalidos. Verifique os detalhes.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                               WebRequest request) {
        String detalhe = String.format(
                "Tipo de parametro invalido. Esperado: %s, Recebido: %s",
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                ex.getValue()
        );
        ExceptionResponse response = new ExceptionResponse(new Date(), "Parametro Invalido", detalhe);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
