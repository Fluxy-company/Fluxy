package school.sptech.emailservice.domain.port.out;

import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.model.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface EmailRepositoryPort {

    Email salvar(Email email);

    Optional<Email> buscarPorId(UUID id);

    List<Email> buscarProntosParaEnvio(int limite);

    Pagina<Email> listar(EmailStatus status, int pagina, int tamanho);
}
