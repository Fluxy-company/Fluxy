package school.sptech.iefcbackend.exception.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import school.sptech.iefcbackend.exception.EmailJaCadastradoException;
import school.sptech.iefcbackend.exception.ExceptionResponse;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ExceptionResponse> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Recurso Não Encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ExceptionResponse> handleEmailJaCadastrado(
            EmailJaCadastradoException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Email Já Cadastrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Credenciais Inválidas",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Erro de Autenticação",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de Validação");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        response.put("erros", errors);
        response.put("mensagem", "Um ou mais campos inválidos. Verifique os detalhes.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {
        String detalhe = "Violação de integridade de dados. Verifique se os dados já existem ou se há referências inválidas.";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("unique constraint")) {
                detalhe = "Dados duplicados. Verifique se este registro já existe.";
            } else if (ex.getMessage().contains("foreign key")) {
                detalhe = "Referência inválida. O registro relacionado não existe.";
            }
        }

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Violação de Integridade de Dados",
                detalhe
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String detalhe = String.format(
                "Tipo de parâmetro inválido. Esperado: %s, Recebido: %s",
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                ex.getValue()
        );

        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Parâmetro Inválido",
                detalhe
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Argumento Inválido",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(
            IllegalStateException ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Estado Inválido",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde."
        );

        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
