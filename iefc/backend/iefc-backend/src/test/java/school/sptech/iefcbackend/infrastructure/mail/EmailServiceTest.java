package school.sptech.iefcbackend.infrastructure.mail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import school.sptech.iefcbackend.domain.event.RelatorioGeradoEvent;
import school.sptech.iefcbackend.infrastructure.messaging.EnviarEmailMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void devePublicarEmailDoRelatorioNoRabbitMq() {
        var service = new EmailService(rabbitTemplate);
        ReflectionTestUtils.setField(service, "destinatario", "admin@iefc.org.br");
        ReflectionTestUtils.setField(service, "remetente", "no-reply@iefc.org.br");
        var evento = new RelatorioGeradoEvent(this, "2026", 2048);

        service.enviarEmailRelatorio(evento);

        var mensagemCaptor = ArgumentCaptor.forClass(EnviarEmailMessage.class);
        verify(rabbitTemplate).convertAndSend(mensagemCaptor.capture());
        assertThat(mensagemCaptor.getValue().destinatarios()).isEqualTo(List.of("admin@iefc.org.br"));
        assertThat(mensagemCaptor.getValue().remetente()).isEqualTo("no-reply@iefc.org.br");
        assertThat(mensagemCaptor.getValue().assunto()).contains("2026");
        assertThat(mensagemCaptor.getValue().corpo()).contains("2 KB");
    }
}
