package school.sptech.fluxybackend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import school.sptech.fluxybackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.models.Usuario;

@Component
public class UsuarioDTOMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setEmail(usuario.getEmail());

        return dto;
    }

    public Usuario toEntity(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
                usuario.setIdUsuario(dto.getIdUsuario());
                usuario.setNome(dto.getNome());
                usuario.setSobrenome(dto.getSobrenome());
                usuario.setEmail(dto.getEmail());
                usuario.setSenha(dto.getSenha());

                return usuario;
    }


}

