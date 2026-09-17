package school.sptech.emailservice.domain.port.in;

import school.sptech.emailservice.domain.model.Email;

public interface SolicitarEnvioEmailUseCase {

    Email solicitar(SolicitarEnvioEmailCommand comando);
}
