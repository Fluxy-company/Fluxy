package school.sptech.iefcbackend.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.events.ColaboradorAnalisadoEvent;
import school.sptech.iefcbackend.events.ColaboradorCadastradoEvent;
import school.sptech.iefcbackend.models.StatusCadastro;
import school.sptech.iefcbackend.services.NotificacaoColaboradorService;

@Component
public class ColaboradorNotificacaoListener {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorNotificacaoListener.class);

    private final NotificacaoColaboradorService notificacaoService;

    public ColaboradorNotificacaoListener(NotificacaoColaboradorService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @EventListener
    public void onColaboradorCadastrado(ColaboradorCadastradoEvent evento) {
        log.info("[ColaboradorNotificacaoListener] Novo colaborador pendente: {}", evento.getColaborador().getEmail());
        notificacaoService.notificarNovoCadastroPendente(evento.getColaborador());
    }

    @EventListener
    public void onColaboradorAnalisado(ColaboradorAnalisadoEvent evento) {
        if (evento.getStatus() == StatusCadastro.APROVADO) {
            log.info("[ColaboradorNotificacaoListener] Colaborador aprovado: {}", evento.getColaborador().getEmail());
            notificacaoService.notificarCadastroAprovado(evento.getColaborador());
        } else if (evento.getStatus() == StatusCadastro.REPROVADO) {
            log.info("[ColaboradorNotificacaoListener] Colaborador reprovado: {}", evento.getColaborador().getEmail());
            notificacaoService.notificarCadastroReprovado(evento.getColaborador());
        }
    }
}
