package school.sptech.iefcbackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import school.sptech.iefcbackend.models.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceNotificacaoAdapter implements NotificacaoColaboradorService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceNotificacaoAdapter.class);

    private static final String TEMPLATE_ADMIN_PENDENTE = "email-templates/admin-novo-colaborador-pendente.html";
    private static final String TEMPLATE_COLABORADOR_STATUS = "email-templates/colaborador-status-cadastro.html";

    private static final String COR_APROVADO = "#1A8A87";
    private static final String COR_REPROVADO = "#C0392B";

    private final RestTemplate restTemplate;

    private final String templateAdminPendente;
    private final String templateColaboradorStatus;

    @Value("${app.email-service.base-url:http://email-service:8080}")
    private String baseUrl;

    @Value("${app.email-service.api-key:}")
    private String apiKey;

    @Value("${app.email-service.remetente:no-reply@iefc.org.br}")
    private String remetente;

    @Value("${app.email-service.admin-destinatario:admin@iefc.org.br}")
    private String adminDestinatario;

    @Value("${app.frontend.base-url:http://localhost:3000}")
    private String frontendBaseUrl;

    public EmailServiceNotificacaoAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-KEY", apiKey);

            Map<String, Object> payload = Map.of(
                    "destinatarios", List.of(destinatario),
                    "remetente", remetente,
                    "assunto", assunto,
                    "corpo", corpoHtml
            );

            restTemplate.postForEntity(baseUrl + "/api/v1/emails", new HttpEntity<>(payload, headers), Void.class);
            log.info("[EmailServiceNotificacaoAdapter] Notificação enfileirada no email-service para {}", destinatario);
        } catch (RestClientException e) {
            log.warn("[EmailServiceNotificacaoAdapter] Não foi possível notificar {} via email-service ({}). "
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
