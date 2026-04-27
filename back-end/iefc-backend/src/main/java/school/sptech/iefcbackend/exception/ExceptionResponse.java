package school.sptech.iefcbackend.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
    //serve para formatar
}
