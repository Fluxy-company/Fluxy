package school.sptech.emailservice.domain.port.in;

import java.util.List;

public record SolicitarEnvioEmailCommand(
        List<String> destinatarios,
        List<String> copia,
        String remetente,
        String assunto,
        String corpo
) {
}
