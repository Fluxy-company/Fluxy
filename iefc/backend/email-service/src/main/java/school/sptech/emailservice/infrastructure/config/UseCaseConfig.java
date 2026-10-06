package school.sptech.emailservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import school.sptech.emailservice.application.usecase.ProcessarFilaEmailsService;
import school.sptech.emailservice.application.usecase.SolicitarEnvioEmailService;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.EmailSenderPort;

@Configuration
public class UseCaseConfig {

    @Value("${app.email.remetente-padrao}")
    private String remetentePadrao;

    @Value("${app.email.max-tentativas:5}")
    private int maxTentativas;

    @Bean
    public SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase(EmailRepositoryPort repository) {
        return new SolicitarEnvioEmailService(repository, remetentePadrao, maxTentativas);
    }

    @Bean
    public ProcessarFilaEmailsUseCase processarFilaEmailsUseCase(EmailRepositoryPort repository,
                                                                   EmailSenderPort sender) {
        return new ProcessarFilaEmailsService(repository, sender);
    }
}
