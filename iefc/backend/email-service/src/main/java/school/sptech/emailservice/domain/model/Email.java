package school.sptech.emailservice.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Email {

    private final UUID id;
    private final List<String> destinatarios;
    private final List<String> copia;
    private final String remetente;
    private final String assunto;
    private final String corpo;
    private final Integer maxTentativas;
    private final LocalDateTime criadoEm;

    private EmailStatus status;
    private Integer tentativas;
    private LocalDateTime atualizadoEm;
    private LocalDateTime enviadoEm;
    private String mensagemErro;

    private Email(UUID id,
                   List<String> destinatarios,
                   List<String> copia,
                   String remetente,
                   String assunto,
                   String corpo,
                   EmailStatus status,
                   Integer tentativas,
                   Integer maxTentativas,
                   LocalDateTime criadoEm,
                   LocalDateTime atualizadoEm,
                   LocalDateTime enviadoEm,
                   String mensagemErro) {
        this.id = id;
        this.destinatarios = destinatarios;
        this.copia = copia;
        this.remetente = remetente;
        this.assunto = assunto;
        this.corpo = corpo;
        this.status = status;
        this.tentativas = tentativas;
        this.maxTentativas = maxTentativas;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.enviadoEm = enviadoEm;
        this.mensagemErro = mensagemErro;
    }

    public static Email novo(List<String> destinatarios,
                              List<String> copia,
                              String remetente,
                              String assunto,
                              String corpo,
                              Integer maxTentativas) {
        Objects.requireNonNull(destinatarios, "destinatarios nao pode ser nulo");
        if (destinatarios.isEmpty()) {
            throw new IllegalArgumentException("E preciso informar ao menos um destinatario.");
        }
        LocalDateTime agora = LocalDateTime.now();
        return new Email(
                UUID.randomUUID(),
                List.copyOf(destinatarios),
                copia == null ? List.of() : List.copyOf(copia),
                remetente,
                assunto,
                corpo,
                EmailStatus.PENDENTE,
                0,
                maxTentativas,
                agora,
                agora,
                null,
                null
        );
    }

    public static Email reconstruir(UUID id,
                                     List<String> destinatarios,
                                     List<String> copia,
                                     String remetente,
                                     String assunto,
                                     String corpo,
                                     EmailStatus status,
                                     int tentativas,
                                     int maxTentativas,
                                     LocalDateTime criadoEm,
                                     LocalDateTime atualizadoEm,
                                     LocalDateTime enviadoEm,
                                     String mensagemErro) {
        return new Email(id, destinatarios, copia, remetente, assunto, corpo,
                status, tentativas, maxTentativas, criadoEm, atualizadoEm, enviadoEm, mensagemErro);
    }

    public void marcarComoEnviando() {
        if (status != EmailStatus.PENDENTE && status != EmailStatus.FALHA) {
            throw new IllegalStateException(
                    "So e possivel iniciar o envio de e-mails com status PENDENTE ou FALHA. Status atual: " + status);
        }
        this.status = EmailStatus.ENVIANDO;
        this.tentativas++;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void marcarComoEnviado() {
        this.status = EmailStatus.ENVIADO;
        this.enviadoEm = LocalDateTime.now();
        this.atualizadoEm = this.enviadoEm;
        this.mensagemErro = null;
    }

    public void marcarComoFalha(String motivo) {
        this.status = EmailStatus.FALHA;
        this.mensagemErro = motivo;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean podeReprocessar() {
        return (status == EmailStatus.PENDENTE || status == EmailStatus.FALHA) && tentativas < maxTentativas;
    }

    public void prepararParaReenvioManual() {
        if (status != EmailStatus.FALHA) {
            throw new IllegalStateException("Somente e-mails com status FALHA podem ser reenviados manualmente.");
        }
        this.status = EmailStatus.PENDENTE;
        this.atualizadoEm = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public List<String> getCopia() {
        return copia;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public Integer getTentativas() {
        return tentativas;
    }

    public Integer getMaxTentativas() {
        return maxTentativas;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public LocalDateTime getEnviadoEm() {
        return enviadoEm;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
}
