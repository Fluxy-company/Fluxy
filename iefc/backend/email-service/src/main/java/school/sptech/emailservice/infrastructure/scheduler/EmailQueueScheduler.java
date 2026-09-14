package school.sptech.emailservice.infrastructure.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;

@Component
public class EmailQueueScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailQueueScheduler.class);

    private final ProcessarFilaEmailsUseCase processarFilaEmailsUseCase;

    @Value("${app.email.scheduler.batch-size:20}")
    private int tamanhoLote;

    public EmailQueueScheduler(ProcessarFilaEmailsUseCase processarFilaEmailsUseCase) {
        this.processarFilaEmailsUseCase = processarFilaEmailsUseCase;
    }

    @Scheduled(fixedDelayString = "${app.email.scheduler.fixed-delay-ms:15000}")
    public void varrerFilaDeEmails() {
        log.debug("[EmailQueueScheduler] Verificando e-mails pendentes/com falha...");
        processarFilaEmailsUseCase.processarPendentes(tamanhoLote);
    }
}
