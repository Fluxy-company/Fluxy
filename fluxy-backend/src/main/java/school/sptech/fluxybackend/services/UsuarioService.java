package school.sptech.fluxybackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.fluxybackend.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.dto.mapper.UsuarioDTOMapper;
import school.sptech.fluxybackend.exception.ResourceNotFoundException;
import school.sptech.fluxybackend.models.Usuario;
import school.sptech.fluxybackend.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    private UsuarioDTOMapper mapper;

    public List<UsuarioResponseDTO> findAll(){
       List<Usuario> usuarios = repository.findAll();
       List<UsuarioResponseDTO> dtos = new ArrayList<>();

       for(Usuario usuario : usuarios){
           dtos.add(mapper.toDTO(usuario));
       }
                return dtos;
    }

    public UsuarioResponseDTO findById(Long id){
       Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sem registros nesse id"));
    return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto){
        Usuario usuario = mapper.toEntity(dto);
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = repository.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto){
       Usuario entity = repository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("Não existe"));
       entity.setNome(dto.getNome());
       entity.setSobrenome(dto.getSobrenome());
       entity.setEmail(dto.getEmail());
       entity.setTelefone(dto.getTelefone());

       Usuario saved = repository.save(entity);
       return mapper.toDTO(saved);
    }

    public void delete(Long id){
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sem registros nesse id"));
        repository.delete(entity);
    }
}
