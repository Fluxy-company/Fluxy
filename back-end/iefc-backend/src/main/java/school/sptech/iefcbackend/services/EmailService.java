package school.sptech.iefcbackend.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.events.RelatorioGeradoEvent;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

    private final JavaMailSender mailSender;

    @Value("${app.relatorio.email.destinatario}")
    private String destinatario;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailRelatorio(RelatorioGeradoEvent evento) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatario);
            mensagem.setSubject("Relatório IEFC " + evento.getAno() + " gerado com sucesso");
            mensagem.setText(
                    "Olá,\n\n" +
                            "O Relatório de Atividades IEFC foi gerado com sucesso.\n\n" +
                            "Ano do Relatório : " + evento.getAno() + "\n" +
                            "Tamanho do PDF   : " + (evento.getTamanhoBytes() / 1024) + " KB\n" +
                            "Gerado em        : " + evento.getGeradoEm().format(FORMATTER) + "\n\n" +
                            "Sistema IEFC"
            );

            mailSender.send(mensagem);
            log.info("[EmailService] E-mail de notificação enviado para: {}", destinatario);

        } catch (Exception e) {
            log.error("[EmailService] Falha ao enviar e-mail: {}", e.getMessage(), e);
        }
    }
}
