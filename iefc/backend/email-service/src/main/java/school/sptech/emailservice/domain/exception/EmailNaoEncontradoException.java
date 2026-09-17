package school.sptech.emailservice.domain.exception;

import java.util.UUID;

public class EmailNaoEncontradoException extends RuntimeException {

    public EmailNaoEncontradoException(UUID id) {
        super("Nenhum e-mail encontrado para o id: " + id);
    }
}
