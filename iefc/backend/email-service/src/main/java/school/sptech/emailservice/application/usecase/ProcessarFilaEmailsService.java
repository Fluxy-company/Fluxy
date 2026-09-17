package school.sptech.emailservice.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.EmailSenderPort;

import java.util.List;
import java.util.UUID;

public class ProcessarFilaEmailsService implements ProcessarFilaEmailsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessarFilaEmailsService.class);

    private final EmailRepositoryPort repository;
    private final EmailSenderPort sender;

    public ProcessarFilaEmailsService(EmailRepositoryPort repository, EmailSenderPort sender) {
        this.repository = repository;
        this.sender = sender;
    }

    @Override
    public void processarPendentes(int lote) {
        List<Email> prontos = repository.buscarProntosParaEnvio(lote);
        if (!prontos.isEmpty()) {
            log.info("[ProcessarFilaEmailsService] {} e-mail(s) prontos para envio nesta varredura", prontos.size());
        }
        prontos.forEach(this::processarUmEmail);
    }

    @Override
    public void processarPorId(UUID id) {
        repository.buscarPorId(id)
                .filter(Email::podeReprocessar)
                .ifPresent(this::processarUmEmail);
    }

    private void processarUmEmail(Email email) {
        email.marcarComoEnviando();
        repository.salvar(email);

        try {
            sender.enviar(email);
            email.marcarComoEnviado();
            log.info("[ProcessarFilaEmailsService] E-mail {} enviado com sucesso (tentativa {}/{})",
                    email.getId(), email.getTentativas(), email.getMaxTentativas());
        } catch (Exception ex) {
            email.marcarComoFalha(ex.getMessage());
            log.warn("[ProcessarFilaEmailsService] Falha ao enviar e-mail {} (tentativa {}/{}): {}",
                    email.getId(), email.getTentativas(), email.getMaxTentativas(), ex.getMessage());
        } finally {
            repository.salvar(email);
        }
    }
}
