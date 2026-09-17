package school.sptech.emailservice.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailCommand;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.NotificadorFilaPort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitarEnvioEmailServiceTest {

    private static final String REMETENTE_PADRAO = "no-reply@iefc.org.br";
    private static final int MAX_TENTATIVAS_PADRAO = 5;

    @Mock
    private EmailRepositoryPort repository;

    @Mock
    private NotificadorFilaPort notificador;

    private SolicitarEnvioEmailService service;

    @BeforeEach
    void setUp() {
        service = new SolicitarEnvioEmailService(repository, notificador, REMETENTE_PADRAO, MAX_TENTATIVAS_PADRAO);
    }

    @Test
    void deveSalvarEmailComoPendenteENotificarAFila() {
        SolicitarEnvioEmailCommand comando = new SolicitarEnvioEmailCommand(
                List.of("aluno@sptech.school"),  null, null,
                "Bem-vindo", "Ola, seja bem-vindo(a)!"
        );

        when(repository.salvar(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Email resultado = service.solicitar(comando);

        assertThat(resultado.getStatus()).isEqualTo(EmailStatus.PENDENTE);
        assertThat(resultado.getRemetente()).isEqualTo(REMETENTE_PADRAO);
        assertThat(resultado.getDestinatarios()).containsExactly("aluno@sptech.school");

        ArgumentCaptor<UUID> idCapturado = ArgumentCaptor.forClass(UUID.class);
        verify(notificador).notificarNovoEmail(idCapturado.capture());
        assertThat(idCapturado.getValue()).isEqualTo(resultado.getId());
    }

    @Test
    void deveUsarRemetenteInformadoQuandoPresente() {
        SolicitarEnvioEmailCommand comando = new SolicitarEnvioEmailCommand(
                List.of("aluno@sptech.school"), null, "contato@iefc.org.br",
                "Assunto", "Corpo"
        );

        when(repository.salvar(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Email resultado = service.solicitar(comando);

        assertThat(resultado.getRemetente()).isEqualTo("contato@iefc.org.br");
    }
}
