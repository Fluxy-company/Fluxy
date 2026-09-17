package school.sptech.iefcbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.models.StatusCadastro;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponseDTO {

        private Long idUsuario;
        private String nome;
        private String email;
        private Set<Role> roles;
        private StatusCadastro status;
}
