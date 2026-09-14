package school.sptech.emailservice.domain.port.in;

import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.model.Pagina;

import java.util.UUID;

public interface ConsultarEmailUseCase {

    Email buscarPorId(UUID id);

    Pagina<Email> listar(EmailStatus statusFiltro, int pagina, int tamanho);
}
