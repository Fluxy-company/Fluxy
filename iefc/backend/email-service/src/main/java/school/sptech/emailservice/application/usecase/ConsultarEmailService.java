package school.sptech.emailservice.application.usecase;

import school.sptech.emailservice.domain.exception.EmailNaoEncontradoException;
import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.model.Pagina;
import school.sptech.emailservice.domain.port.in.ConsultarEmailUseCase;
import school.sptech.emailservice.domain.port.out.EmailRepositoryPort;

import java.util.UUID;

public class ConsultarEmailService implements ConsultarEmailUseCase {

    private final EmailRepositoryPort repository;

    public ConsultarEmailService(EmailRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Email buscarPorId(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new EmailNaoEncontradoException(id));
    }

    @Override
    public Pagina<Email> listar(EmailStatus statusFiltro, int pagina, int tamanho) {
        return repository.listar(statusFiltro, pagina, tamanho);
    }
}
