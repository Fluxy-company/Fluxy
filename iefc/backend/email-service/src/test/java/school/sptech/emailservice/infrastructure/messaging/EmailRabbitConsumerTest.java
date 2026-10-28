package school.sptech.emailservice.infrastructure.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.port.in.ProcessarFilaEmailsUseCase;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailCommand;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailRabbitConsumerTest {

    @Mock
    private SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase;

    @Mock
    private ProcessarFilaEmailsUseCase processarFilaEmailsUseCase;

    @Test
    void devePersistirEProcessarEmailRecebidoPelaFila() {
        var emailId = UUID.randomUUID();
        var email = Email.reconstruir(
                emailId,
                List.of("destinatario@iefc.org.br"),
                List.of("copia@iefc.org.br"),
                "no-reply@iefc.org.br",
                "Assunto",
                "Corpo",
                null,
                0,
                3,
                null,
                null,
                null,
                null
        );
        when(solicitarEnvioEmailUseCase.solicitar(org.mockito.ArgumentMatchers.any()))
                .thenReturn(email);
        var consumer = new EmailRabbitConsumer(solicitarEnvioEmailUseCase, processarFilaEmailsUseCase);
        var mensagem = new EnviarEmailMessage(
                List.of("destinatario@iefc.org.br"),
                List.of("copia@iefc.org.br"),
                "no-reply@iefc.org.br",
                "Assunto",
                "Corpo"
        );

        consumer.receive(mensagem);

        var comandoCaptor = ArgumentCaptor.forClass(SolicitarEnvioEmailCommand.class);
        verify(solicitarEnvioEmailUseCase).solicitar(comandoCaptor.capture());
        assertThat(comandoCaptor.getValue().destinatarios()).isEqualTo(mensagem.destinatarios());
        assertThat(comandoCaptor.getValue().copia()).isEqualTo(mensagem.copia());
        assertThat(comandoCaptor.getValue().remetente()).isEqualTo(mensagem.remetente());
        assertThat(comandoCaptor.getValue().assunto()).isEqualTo(mensagem.assunto());
        assertThat(comandoCaptor.getValue().corpo()).isEqualTo(mensagem.corpo());
        verify(processarFilaEmailsUseCase).processarPorId(emailId);
    }
}
