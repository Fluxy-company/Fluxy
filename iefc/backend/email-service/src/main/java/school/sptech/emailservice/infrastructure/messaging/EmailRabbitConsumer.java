package school.sptech.emailservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailCommand;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;

@Service
@RequiredArgsConstructor
public class EmailRabbitConsumer {

    private final SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase;
    private final ProcessarFilaEmailsUseCase processarFilaEmailsUseCase;

    @RabbitListener(queues = "${broker.queue.name}")
    public void receive(EnviarEmailMessage message) {
        var email = solicitarEnvioEmailUseCase.solicitar(new SolicitarEnvioEmailCommand(
                message.destinatarios(),
                message.copia(),
                message.remetente(),
                message.assunto(),
                message.corpo()
        ));
        processarFilaEmailsUseCase.processarPorId(email.getId());
    }
}
