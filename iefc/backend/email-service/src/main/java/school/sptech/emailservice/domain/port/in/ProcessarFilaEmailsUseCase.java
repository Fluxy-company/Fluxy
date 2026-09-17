package school.sptech.emailservice.domain.port.in;

import java.util.UUID;

public interface ProcessarFilaEmailsUseCase {

    void processarPendentes(int lote);

    void processarPorId(UUID id);
}
