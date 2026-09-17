package school.sptech.iefcbackend.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.dto.UsuarioAdminRequestDTO;
import school.sptech.iefcbackend.dto.UsuarioColaboradorRequestDTO;
import school.sptech.iefcbackend.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.dto.UsuarioResponseDTO;
import school.sptech.iefcbackend.dto.mapper.UsuarioDTOMapper;
import school.sptech.iefcbackend.events.ColaboradorAnalisadoEvent;
import school.sptech.iefcbackend.events.ColaboradorCadastradoEvent;
import school.sptech.iefcbackend.exception.EmailJaCadastradoException;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.StatusCadastro;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioDTOMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UsuarioService(UsuarioRepository repository, UsuarioDTOMapper mapper,
            BCryptPasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public List<UsuarioResponseDTO> buscarTodos() {
        List<Usuario> usuarios = repository.findAll();
        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            dtos.add(mapper.toDTO(usuario));
        }
        return dtos;
    }

    public UsuarioResponseDTO acharPeloId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorEmail(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Email não encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = repository.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO salvarUsuarioComoAdmin(UsuarioAdminRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = repository.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO atualizarUsuarioPorId(Long id, UsuarioRequestDTO dto) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe"));

        if (!entity.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário");
        }

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        // Atualizar senha se fornecida
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public void deletarPorId(Long id) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        repository.delete(entity);
    }

    public List<UsuarioResponseDTO> buscarTodosAdmins() {
        List<Usuario> admins = repository.findAllAdmins();
        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for (Usuario admin : admins) {
            dtos.add(mapper.toDTO(admin));
        }
        return dtos;
    }

    /** Autocadastro público de colaborador (funcionário). Nasce PENDENTE. */
    public UsuarioResponseDTO cadastrarColaborador(UsuarioColaboradorRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = repository.save(usuario);

        eventPublisher.publishEvent(new ColaboradorCadastradoEvent(this, saved));

        return mapper.toDTO(saved);
    }

    /** Lista todos os cadastros PENDENTE, para a tela de aprovação do admin. */
    public List<UsuarioResponseDTO> listarPendentes() {
        List<Usuario> pendentes = repository.findByStatus(StatusCadastro.PENDENTE);
        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for (Usuario usuario : pendentes) {
            dtos.add(mapper.toDTO(usuario));
        }
        return dtos;
    }

    public UsuarioResponseDTO aprovarCadastro(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));

        usuario.aprovar();
        Usuario saved = repository.save(usuario);

        eventPublisher.publishEvent(new ColaboradorAnalisadoEvent(this, saved));

        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO reprovarCadastro(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));

        usuario.reprovar();
        Usuario saved = repository.save(usuario);

        eventPublisher.publishEvent(new ColaboradorAnalisadoEvent(this, saved));

        return mapper.toDTO(saved);
    }
}
