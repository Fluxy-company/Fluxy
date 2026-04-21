package school.sptech.iefcbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponseDTO {

        private Long idUsuario;
        private String nome;
        private String sobrenome;
        private String email;
}
