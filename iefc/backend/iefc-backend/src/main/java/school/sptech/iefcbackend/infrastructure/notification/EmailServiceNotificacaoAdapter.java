package school.sptech.iefcbackend.infrastructure.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.port.NotificacaoColaboradorPort;
import school.sptech.iefcbackend.infrastructure.messaging.EnviarEmailMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailServiceNotificacaoAdapter implements NotificacaoColaboradorPort {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceNotificacaoAdapter.class);

    private static final String TEMPLATE_ADMIN_PENDENTE = "email-templates/admin-novo-colaborador-pendente.html";
    private static final String TEMPLATE_COLABORADOR_STATUS = "email-templates/colaborador-status-cadastro.html";

    private static final String COR_APROVADO = "#1A8A87";
    private static final String COR_REPROVADO = "#C0392B";

    private final RabbitTemplate rabbitTemplate;

    private final String templateAdminPendente;
    private final String templateColaboradorStatus;

    @Value("${app.email-service.remetente:no-reply@iefc.org.br}")
    private String remetente;

    @Value("${app.email-service.admin-destinatario:admin@iefc.org.br}")
    private String adminDestinatario;

    @Value("${app.frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;

    public EmailServiceNotificacaoAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.templateAdminPendente = carregarTemplate(TEMPLATE_ADMIN_PENDENTE);
        this.templateColaboradorStatus = carregarTemplate(TEMPLATE_COLABORADOR_STATUS);
    }

    @Override
    public void notificarNovoCadastroPendente(Usuario colaborador) {
        var assunto = "Novo colaborador aguardando aprovação: " + colaborador.getNome();
        var corpo = templateAdminPendente
                .replace("{{NOME_COLABORADOR}}", colaborador.getNome())
                .replace("{{EMAIL_COLABORADOR}}", colaborador.getEmail())
                .replace("{{PAINEL_URL}}", frontendBaseUrl + "/admin/colaboradores/pendentes");

        enviar(adminDestinatario, assunto, corpo);
    }

    @Override
    public void notificarCadastroAprovado(Usuario colaborador) {
        var assunto = "Seu cadastro foi aprovado";
        var corpo = montarCorpoStatus(
                colaborador,
                "Cadastro aprovado!",
                "Seu cadastro no sistema IEFC foi aprovado pelo administrador. Você já pode fazer login normalmente.",
                COR_APROVADO,
                "block",
                frontendBaseUrl + "/login"
        );

        enviar(colaborador.getEmail(), assunto, corpo);
    }

    @Override
    public void notificarCadastroReprovado(Usuario colaborador) {
        var assunto = "Seu cadastro não foi aprovado";
        var corpo = montarCorpoStatus(
                colaborador,
                "Cadastro não aprovado",
                "Seu cadastro no sistema IEFC não foi aprovado pelo administrador. "
                        + "Se você acredita que isso é um engano, entre em contato com o administrador.",
                COR_REPROVADO,
                "none",
                frontendBaseUrl + "/login"
        );

        enviar(colaborador.getEmail(), assunto, corpo);
    }

    private String montarCorpoStatus(Usuario colaborador, String titulo, String mensagem,
            String cor, String botaoDisplay, String loginUrl) {
        return templateColaboradorStatus
                .replace("{{NOME_COLABORADOR}}", colaborador.getNome())
                .replace("{{STATUS_TITULO}}", titulo)
                .replace("{{STATUS_MENSAGEM}}", mensagem)
                .replace("{{STATUS_COR}}", cor)
                .replace("{{BOTAO_DISPLAY}}", botaoDisplay)
                .replace("{{LOGIN_URL}}", loginUrl);
    }

    private void enviar(String destinatario, String assunto, String corpoHtml) {
        try {
            rabbitTemplate.convertAndSend(new EnviarEmailMessage(
                    List.of(destinatario), null, remetente, assunto, corpoHtml));
            log.info("[EmailServiceNotificacaoAdapter] Notificação publicada na fila para {}", destinatario);
        } catch (AmqpException e) {
            log.warn("[EmailServiceNotificacaoAdapter] Não foi possível publicar notificação para {} ({}). "
                    + "Seguindo sem bloquear o fluxo.", destinatario, e.getMessage());
        }
    }

    private String carregarTemplate(String caminhoClasspath) {
        try {
            var resource = new ClassPathResource(caminhoClasspath);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
        
            log.error("[EmailServiceNotificacaoAdapter] Não foi possível carregar o template {}: {}",
                    caminhoClasspath, e.getMessage());
            return "{{NOME_COLABORADOR}} — {{STATUS_TITULO}} {{STATUS_MENSAGEM}} {{EMAIL_COLABORADOR}}";
        }
    }
}

