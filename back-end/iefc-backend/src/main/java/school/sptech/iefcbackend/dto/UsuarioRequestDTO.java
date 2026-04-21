package school.sptech.iefcbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
        private Long idUsuario;
        @NotBlank(message = "Nome é obrigatório")
        private String nome;
        @NotBlank(message = "Sobrenome é obrigatório")
        private String sobrenome;
        @Email
        @NotBlank
        private String email;

        private String senha;
        private Long createdAt;
    }
