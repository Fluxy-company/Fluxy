package school.sptech.emailservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import school.sptech.emailservice.application.usecase.ConsultarEmailService;
import school.sptech.emailservice.application.usecase.ProcessarFilaEmailsService;
import school.sptech.emailservice.application.usecase.ReenviarEmailService;
import school.sptech.emailservice.application.usecase.SolicitarEnvioEmailService;
import school.sptech.emailservice.domain.port.in.ConsultarEmailUseCase;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;
import school.sptech.emailservice.domain.port.in.ReenviarEmailUseCase;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.EmailSenderPort;
import school.sptech.emailservice.domain.port.out.NotificadorFilaPort;

@Configuration
public class UseCaseConfig {

    @Value("${app.email.remetente-padrao}")
    private String remetentePadrao;

    @Value("${app.email.max-tentativas:5}")
    private int maxTentativas;

    @Bean
    public SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase(EmailRepositoryPort repository,
                                                                  NotificadorFilaPort notificador) {
        return new SolicitarEnvioEmailService(repository, notificador, remetentePadrao, maxTentativas);
    }

    @Bean
    public ConsultarEmailUseCase consultarEmailUseCase(EmailRepositoryPort repository) {
        return new ConsultarEmailService(repository);
    }

    @Bean
    public ReenviarEmailUseCase reenviarEmailUseCase(EmailRepositoryPort repository,
                                                       NotificadorFilaPort notificador) {
        return new ReenviarEmailService(repository, notificador);
    }

    @Bean
    public ProcessarFilaEmailsUseCase processarFilaEmailsUseCase(EmailRepositoryPort repository,
                                                                   EmailSenderPort sender) {
        return new ProcessarFilaEmailsService(repository, sender);
    }
}
