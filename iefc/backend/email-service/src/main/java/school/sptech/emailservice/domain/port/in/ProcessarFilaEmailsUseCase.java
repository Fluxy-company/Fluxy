package school.sptech.emailservice.domain.port.in;

import java.util.UUID;

public interface ProcessarFilaEmailsUseCase {

    void processarPorId(UUID id);
}
