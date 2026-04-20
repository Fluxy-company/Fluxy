package school.sptech.iefcbackend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.iefcbackend.models.Usuario;

@Component
public class UsuarioDTOMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setEmail(usuario.getEmail());

        return dto;
    }

    public Usuario toEntity(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
                usuario.setId(dto.getIdUsuario());
                usuario.setNome(dto.getNome());
                usuario.setSobrenome(dto.getSobrenome());
                usuario.setEmail(dto.getEmail());
                usuario.setSenha(dto.getSenha());

                return usuario;
    }


}

