package school.sptech.emailservice.domain.port.out;

import school.sptech.emailservice.domain.model.Email;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepositoryPort {

    Email salvar(Email email);

    Optional<Email> buscarPorId(UUID id);
}
