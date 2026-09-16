package school.sptech.emailservice.infrastructure.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;

@Component
public class EmailCriadoAsyncListener {

    private static final Logger log = LoggerFactory.getLogger(EmailCriadoAsyncListener.class);

    private final ProcessarFilaEmailsUseCase processarFilaEmailsUseCase;

    public EmailCriadoAsyncListener(ProcessarFilaEmailsUseCase processarFilaEmailsUseCase) {
        this.processarFilaEmailsUseCase = processarFilaEmailsUseCase;
    }

    @Async
    @EventListener
    public void aoReceberNovoEmail(EmailCriadoEvent evento) {
        log.info("[EmailCriadoAsyncListener] Processando e-mail {} de forma assincrona", evento.id());
        processarFilaEmailsUseCase.processarPorId(evento.id());
    }
}
