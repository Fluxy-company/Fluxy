package school.sptech.emailservice.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SolicitarEnvioEmailRequestDTO(

        @NotEmpty(message = "Informe ao menos um destinatario.")
        List<@Email(message = "Destinatario invalido.") String> destinatarios,

        List<@Email(message = "E-mail em copia invalido.") String> copia,

        @Email(message = "Remetente invalido.")
        String remetente,

        @NotBlank(message = "O assunto e obrigatorio.")
        @Size(max = 250, message = "O assunto deve ter no maximo 250 caracteres.")
        String assunto,

        @NotBlank(message = "O corpo do e-mail e obrigatorio.")
        String corpo
) {
}
