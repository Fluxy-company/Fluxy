package school.sptech.iefcbackend.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.events.RelatorioGeradoEvent;
import school.sptech.iefcbackend.services.EmailService;

@Component
public class RelatorioEmailListener {

    private static final Logger log = LoggerFactory.getLogger(RelatorioEmailListener.class);

    private final EmailService emailService;

    public RelatorioEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void onRelatorioGerado(RelatorioGeradoEvent evento) {
        log.info("[RelatorioEmailListener] Relatório gerado para o ano {}. Enviando e-mail...",
                evento.getAno());
        emailService.enviarEmailRelatorio(evento);
    }
}
