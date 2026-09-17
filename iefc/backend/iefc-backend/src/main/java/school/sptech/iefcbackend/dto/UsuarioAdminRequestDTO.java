package school.sptech.iefcbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.iefcbackend.models.Role;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAdminRequestDTO {
    @NotBlank(message = "Nome completo é obrigatório")
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotNull(message = "Role é obrigatória")
    private Role role;
}
