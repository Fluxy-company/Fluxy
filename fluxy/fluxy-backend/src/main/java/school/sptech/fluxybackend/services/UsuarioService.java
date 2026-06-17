package school.sptech.fluxybackend.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import school.sptech.fluxybackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.controllers.dto.mapper.UsuarioDTOMapper;
import school.sptech.fluxybackend.exception.EmailJaCadastradoException;
import school.sptech.fluxybackend.exception.RecursoNaoEncontradoException;
import school.sptech.fluxybackend.models.Usuario;
import school.sptech.fluxybackend.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioDTOMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, UsuarioDTOMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UsuarioResponseDTO> buscarTodos(){
       List<Usuario> usuarios = repository.findAll();
       List<UsuarioResponseDTO> dtos = new ArrayList<>();

       for(Usuario usuario : usuarios){
           dtos.add(mapper.toDTO(usuario));
       }
                return dtos;
    }

    public UsuarioResponseDTO acharPeloId(Long id){
       Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
    return mapper.toDTO(usuario);
    }
    
    public UsuarioResponseDTO buscarUsuarioPorEmail(String email){
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Email não encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto){
        if(repository.existsByEmail(dto.getEmail())){
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setCreatedAt(System.currentTimeMillis());

        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario saved = repository.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO atualizarUsuarioPorId(Long id, UsuarioRequestDTO dto){
       Usuario entity = repository.findById(id)
           .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe"));
       
       if (!entity.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
           throw new EmailJaCadastradoException("Email já cadastrado por outro usuário");
       }
       
       entity.setNome(dto.getNome());
       entity.setSobrenome(dto.getSobrenome());
       entity.setEmail(dto.getEmail());
       if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
           entity.setSenha(passwordEncoder.encode(dto.getSenha()));
       }

       Usuario saved = repository.save(entity);
       return mapper.toDTO(saved);
    }

    public void deletarPorId(Long id){
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        repository.delete(entity);
    }
}
