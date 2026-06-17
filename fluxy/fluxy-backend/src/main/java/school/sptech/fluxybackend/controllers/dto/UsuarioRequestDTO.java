package school.sptech.fluxybackend.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;
        
        private String sobrenome;
        
        @Email(message = "Email deve ser válido")
        @NotBlank(message = "Email é obrigatório")
        private String email;
        
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
        private String senha;
    }
