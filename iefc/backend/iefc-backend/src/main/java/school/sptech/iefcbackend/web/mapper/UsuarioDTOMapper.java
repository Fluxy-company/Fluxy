package school.sptech.iefcbackend.web.mapper;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.enums.Role;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;
import school.sptech.iefcbackend.web.dto.UsuarioAdminRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioColaboradorRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioResponseDTO;

import java.util.Set;

@Component
public class UsuarioDTOMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRoles(usuario.getRoles());
        dto.setStatus(usuario.getStatus());

        return dto;
    }

    public Usuario toEntity(UsuarioColaboradorRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setRoles(Set.of(Role.COLABORADOR));
        usuario.setStatus(StatusCadastro.PENDENTE);

        return usuario;
    }

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setRoles(Set.of(Role.ALUNO));

        return usuario;
    }

    public Usuario toEntity(UsuarioAdminRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setRoles(Set.of(dto.getRole()));

        return usuario;
    }
}
