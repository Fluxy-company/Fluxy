package school.sptech.emailservice.infrastructure.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;

/**
 * E este componente que torna o envio "assincrono" do ponto de vista de quem
 * chama a API: o controller apenas persiste o e-mail (status PENDENTE) e
 * retorna 202 Accepted; o envio de fato acontece aqui, em uma thread separada
 * do pool "emailTaskExecutor" (ver AsyncConfig), sem bloquear a requisicao HTTP.
 *
 * O scheduler (EmailQueueScheduler) e a rede de seguranca complementar: cobre
 * casos em que este listener falhar silenciosamente ou a aplicacao reiniciar
 * com e-mails ainda pendentes.
 */
@Component
public class EmailCriadoAsyncListener {

    private static final Logger log = LoggerFactory.getLogger(EmailCriadoAsyncListener.class);

    private final ProcessarFilaEmailsUseCase processarFilaEmailsUseCase;

    public EmailCriadoAsyncListener(ProcessarFilaEmailsUseCase processarFilaEmailsUseCase) {
        this.processarFilaEmailsUseCase = processarFilaEmailsUseCase;
    }

    @Async("emailTaskExecutor")
    @EventListener
    public void aoReceberNovoEmail(EmailCriadoEvent evento) {
        log.info("[EmailCriadoAsyncListener] Processando e-mail {} de forma assincrona", evento.id());
        processarFilaEmailsUseCase.processarPorId(evento.id());
    }
}
