package school.sptech.emailservice.infrastructure.web.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
