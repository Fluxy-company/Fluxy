package school.sptech.emailservice.domain.port.in;

import school.sptech.emailservice.domain.model.Email;

import java.util.UUID;

public interface ReenviarEmailUseCase {

    Email reenviar(UUID id);
}
