package school.sptech.emailservice.domain.port.out;

import school.sptech.emailservice.domain.exception.EnvioEmailException;
import school.sptech.emailservice.domain.model.Email;

public interface EmailSenderPort {

    void enviar(Email email) throws EnvioEmailException;
}
