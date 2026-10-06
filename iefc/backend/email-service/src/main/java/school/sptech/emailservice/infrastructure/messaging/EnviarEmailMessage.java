package school.sptech.emailservice.infrastructure.messaging;

import java.util.List;

public record EnviarEmailMessage(
        List<String> destinatarios,
        List<String> copia,
        String remetente,
        String assunto,
        String corpo
) {
}
