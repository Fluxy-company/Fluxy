package school.sptech.fluxybackend.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import school.sptech.fluxybackend.exception.EmailJaCadastradoException;
import school.sptech.fluxybackend.exception.ExceptionResponse;
import school.sptech.fluxybackend.exception.RecursoNaoEncontradoException;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public final ResponseEntity<ExceptionResponse> hadleEmailAlreadyExistsException(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}
