package school.sptech.emailservice.domain.port.out;

import java.util.UUID;

public interface NotificadorFilaPort {

    void notificarNovoEmail(UUID id);
}
