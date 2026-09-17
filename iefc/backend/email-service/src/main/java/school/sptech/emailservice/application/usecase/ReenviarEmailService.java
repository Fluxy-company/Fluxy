package school.sptech.emailservice.application.usecase;

import school.sptech.emailservice.domain.exception.EmailNaoEncontradoException;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.in.ReenviarEmailUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.NotificadorFilaPort;

import java.util.UUID;

public class ReenviarEmailService implements ReenviarEmailUseCase {

    private final EmailRepositoryPort repository;
    private final NotificadorFilaPort notificador;

    public ReenviarEmailService(EmailRepositoryPort repository, NotificadorFilaPort notificador) {
        this.repository = repository;
        this.notificador = notificador;
    }

    @Override
    public Email reenviar(UUID id) {
        Email email = repository.buscarPorId(id)
                .orElseThrow(() -> new EmailNaoEncontradoException(id));

        email.prepararParaReenvioManual();
        Email salvo = repository.salvar(email);
        notificador.notificarNovoEmail(salvo.getId());
        return salvo;
    }
}
