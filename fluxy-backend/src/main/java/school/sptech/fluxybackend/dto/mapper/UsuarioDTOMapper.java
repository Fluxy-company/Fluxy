package school.sptech.fluxybackend.dto.mapper;

import org.springframework.stereotype.Component;
import school.sptech.fluxybackend.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.models.Usuario;

@Component
public class UsuarioDTOMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());

        return dto;
    }

    public Usuario toEntity(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
                usuario.setId(dto.getId());
                usuario.setNome(dto.getNome());
                usuario.setSobrenome(dto.getSobrenome());
                usuario.setEmail(dto.getEmail());
                usuario.setSenha(dto.getSenha());
                usuario.setTelefone(dto.getTelefone());

                return usuario;
    }


}

