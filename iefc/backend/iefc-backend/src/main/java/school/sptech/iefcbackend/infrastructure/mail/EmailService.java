package school.sptech.iefcbackend.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.event.RelatorioGeradoEvent;
import school.sptech.iefcbackend.infrastructure.messaging.EnviarEmailMessage;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.relatorio.email.destinatario}")
    private String destinatario;

    @Value("${app.email-service.remetente:no-reply@iefc.org.br}")
    private String remetente;

    public EmailService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarEmailRelatorio(RelatorioGeradoEvent evento) {
        try {
            String corpo = "Olá,\n\n" +
                    "O Relatório de Atividades IEFC foi gerado com sucesso.\n\n" +
                    "Ano do Relatório : " + evento.getAno() + "\n" +
                    "Tamanho do PDF   : " + (evento.getTamanhoBytes() / 1024) + " KB\n" +
                    "Gerado em        : " + evento.getGeradoEm().format(FORMATTER) + "\n\n" +
                    "Sistema IEFC";

            rabbitTemplate.convertAndSend(new EnviarEmailMessage(
                    List.of(destinatario),
                    null,
                    remetente,
                    "Relatório IEFC " + evento.getAno() + " gerado com sucesso",
                    corpo
            ));
            log.info("[EmailService] E-mail de relatório enviado para a fila: {}", destinatario);

        } catch (AmqpException e) {
            log.error("[EmailService] Falha ao publicar e-mail na fila: {}", e.getMessage(), e);
        }
    }
}
