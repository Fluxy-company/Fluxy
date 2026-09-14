package school.sptech.emailservice.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import school.sptech.emailservice.domain.exception.EnvioEmailException;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.out.EmailSenderPort;


@Component
public class JavaMailEmailSenderAdapter implements EmailSenderPort {

    private static final Logger log = LoggerFactory.getLogger(JavaMailEmailSenderAdapter.class);

    private final JavaMailSender mailSender;

    public JavaMailEmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(Email email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(email.getRemetente());
            helper.setTo(email.getDestinatarios().toArray(new String[0]));

            if (!email.getCopia().isEmpty()) {
                helper.setCc(email.getCopia().toArray(new String[0]));
            }

            helper.setSubject(email.getAssunto());
        
            helper.setText(email.getCorpo(), true);

            mailSender.send(mimeMessage);

            log.info("[JavaMailEmailSenderAdapter] E-mail {} enviado com sucesso para {}",
                    email.getId(), email.getDestinatarios());
        } catch (MessagingException | MailException ex) {
            log.error("[JavaMailEmailSenderAdapter] Falha ao enviar e-mail {}: {}",
                    email.getId(), ex.getMessage());
            throw new EnvioEmailException("Falha ao enviar e-mail via Mailtrap/SMTP: " + ex.getMessage(), ex);
        }
    }
}
