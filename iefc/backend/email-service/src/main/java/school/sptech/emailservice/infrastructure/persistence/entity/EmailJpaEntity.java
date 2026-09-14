package school.sptech.emailservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import school.sptech.emailservice.domain.model.EmailStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tb_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailJpaEntity {

    @Id
    @Column(name = "email_id")
    private UUID id;

    @Column(name = "destinatarios", length = 2000, nullable = false)
    private String destinatarios;

    @Column(name = "copia", length = 2000)
    private String copia;

    @Column(name = "remetente", length = 150, nullable = false)
    private String remetente;

    @Column(name = "assunto", length = 250, nullable = false)
    private String assunto;

    @Lob
    @Column(name = "corpo", nullable = false)
    private String corpo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private EmailStatus status;

    @Column(name = "tentativas", nullable = false)
    private Integer tentativas;

    @Column(name = "max_tentativas", nullable = false)
    private Integer maxTentativas;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "enviado_em")
    private LocalDateTime enviadoEm;

    @Column(name = "mensagem_erro", length = 1000)
    private String mensagemErro;
}
