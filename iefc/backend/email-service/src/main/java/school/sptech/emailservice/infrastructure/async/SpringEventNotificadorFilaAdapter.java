package school.sptech.emailservice.infrastructure.async;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.port.out.NotificadorFilaPort;

import java.util.UUID;

@Component
public class SpringEventNotificadorFilaAdapter implements NotificadorFilaPort {

    private final ApplicationEventPublisher publisher;

    public SpringEventNotificadorFilaAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void notificarNovoEmail(UUID id) {
        publisher.publishEvent(new EmailCriadoEvent(id));
    }
}
