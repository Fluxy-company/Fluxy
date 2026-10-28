package school.sptech.emailservice.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.emailservice.domain.exception.EnvioEmailException;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;
import school.sptech.emailservice.domain.port.out.EmailSenderPort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessarFilaEmailsServiceTest {

    @Mock
    private EmailRepositoryPort repository;

    @Mock
    private EmailSenderPort sender;

    private ProcessarFilaEmailsService service;

    @BeforeEach
    void setUp() {
        service = new ProcessarFilaEmailsService(repository, sender);
    }

    @Test
    void deveMarcarComoEnviadoQuandoEnvioTemSucesso() {
        Email email = Email.novo(List.of("aluno@sptech.school"), null,
                "no-reply@iefc.org.br", "Assunto", "Corpo", 5);

        when(repository.buscarPorId(email.getId())).thenReturn(Optional.of(email));
        when(repository.salvar(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(sender).enviar(any(Email.class));

        service.processarPorId(email.getId());

        assertThat(email.getStatus()).isEqualTo(EmailStatus.ENVIADO);
        assertThat(email.getTentativas()).isEqualTo(1);
        verify(repository, times(2)).salvar(email);
    }

    @Test
    void deveMarcarComoFalhaQuandoEnvioLancaExcecao() {
        Email email = Email.novo(List.of("aluno@sptech.school"), null,
                "no-reply@iefc.org.br", "Assunto", "Corpo", 5);

        when(repository.buscarPorId(email.getId())).thenReturn(Optional.of(email));
        when(repository.salvar(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new EnvioEmailException("SMTP indisponivel")).when(sender).enviar(any(Email.class));

        service.processarPorId(email.getId());

        assertThat(email.getStatus()).isEqualTo(EmailStatus.FALHA);
        assertThat(email.getMensagemErro()).isEqualTo("SMTP indisponivel");
    }

    @Test
    void processarPorIdDeveIgnorarEmailQueNaoPodeSerReprocessado() {
        Email email = Email.novo(List.of("aluno@sptech.school"), null,
                "no-reply@iefc.org.br", "Assunto", "Corpo", 1);
        email.marcarComoEnviando();
        email.marcarComoEnviado();

        when(repository.buscarPorId(email.getId())).thenReturn(Optional.of(email));

        service.processarPorId(email.getId());

        verify(sender, times(0)).enviar(any(Email.class));
    }
}
