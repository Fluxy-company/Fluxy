package school.sptech.emailservice.application.usecase;

import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailCommand;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.NotificadorFilaPort;

public class SolicitarEnvioEmailService implements SolicitarEnvioEmailUseCase {

    private final EmailRepositoryPort repository;
    private final NotificadorFilaPort notificador;
    private final String remetentePadrao;
    private final Integer maxTentativasPadrao;

    public SolicitarEnvioEmailService(EmailRepositoryPort repository,
                                       NotificadorFilaPort notificador,
                                       String remetentePadrao,
                                       Integer maxTentativasPadrao) {
        this.repository = repository;
        this.notificador = notificador;
        this.remetentePadrao = remetentePadrao;
        this.maxTentativasPadrao = maxTentativasPadrao;
    }

    @Override
    public Email solicitar(SolicitarEnvioEmailCommand comando) {
        String remetente = (comando.remetente() == null || comando.remetente().isBlank())
                ? remetentePadrao
                : comando.remetente();

        Email email = Email.novo(
                comando.destinatarios(),
                comando.copia(),
                remetente,
                comando.assunto(),
                comando.corpo(),
                maxTentativasPadrao
        );

        Email salvo = repository.salvar(email);
        notificador.notificarNovoEmail(salvo.getId());
        return salvo;
    }
}
