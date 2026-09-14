package school.sptech.emailservice.infrastructure.web.dto;

import school.sptech.emailservice.domain.model.EmailStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EmailResponseDTO(
        UUID id,
        List<String> destinatarios,
        List<String> copia,
        String remetente,
        String assunto,
        EmailStatus status,
        Integer tentativas,
        Integer maxTentativas,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        LocalDateTime enviadoEm,
        String mensagemErro
) {
}
