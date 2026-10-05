package school.sptech.iefcbackend.usecase.usuario;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;
import school.sptech.iefcbackend.domain.event.ColaboradorAnalisadoEvent;
import school.sptech.iefcbackend.domain.event.ColaboradorCadastradoEvent;
import school.sptech.iefcbackend.domain.exception.EmailJaCadastradoException;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;
import school.sptech.iefcbackend.web.dto.UsuarioAdminRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioColaboradorRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioResponseDTO;
import school.sptech.iefcbackend.web.mapper.UsuarioDTOMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final UsuarioDTOMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                          UsuarioDTOMapper mapper,
                          BCryptPasswordEncoder passwordEncoder,
                          ApplicationEventPublisher eventPublisher) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public Page<UsuarioResponseDTO> buscarTodos(Pageable pageable) {
        return usuarioRepositoryPort.findAll(pageable).map(mapper::toDTO);
    }

    public UsuarioResponseDTO acharPeloId(Long id) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Email não encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepositoryPort.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = usuarioRepositoryPort.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO salvarUsuarioComoAdmin(UsuarioAdminRequestDTO dto) {
        if (usuarioRepositoryPort.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = usuarioRepositoryPort.save(usuario);
        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO atualizarUsuarioPorId(Long id, UsuarioRequestDTO dto) {
        Usuario entity = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe"));

        if (!entity.getEmail().equals(dto.getEmail()) && usuarioRepositoryPort.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário");
        }

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario saved = usuarioRepositoryPort.save(entity);
        return mapper.toDTO(saved);
    }

    public void deletarPorId(Long id) {
        Usuario entity = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));
        usuarioRepositoryPort.delete(entity);
    }

    public List<UsuarioResponseDTO> buscarTodosAdmins() {
        List<Usuario> admins = usuarioRepositoryPort.findAllAdmins();
        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for (Usuario admin : admins) {
            dtos.add(mapper.toDTO(admin));
        }
        return dtos;
    }

    public UsuarioResponseDTO cadastrarColaborador(UsuarioColaboradorRequestDTO dto) {
        if (usuarioRepositoryPort.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCreatedAt(System.currentTimeMillis());
        Usuario saved = usuarioRepositoryPort.save(usuario);

        eventPublisher.publishEvent(new ColaboradorCadastradoEvent(this, saved));

        return mapper.toDTO(saved);
    }

    public List<UsuarioResponseDTO> listarPendentes() {
        List<Usuario> pendentes = usuarioRepositoryPort.findByStatus(StatusCadastro.PENDENTE);
        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for (Usuario usuario : pendentes) {
            dtos.add(mapper.toDTO(usuario));
        }
        return dtos;
    }

    public Page<UsuarioResponseDTO> listarPendentesPaginado(Pageable pageable) {
        return usuarioRepositoryPort.findByStatus(StatusCadastro.PENDENTE, pageable).map(mapper::toDTO);
    }

    public UsuarioResponseDTO aprovarCadastro(Long id) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));

        usuario.aprovar();
        Usuario saved = usuarioRepositoryPort.save(usuario);

        eventPublisher.publishEvent(new ColaboradorAnalisadoEvent(this, saved));

        return mapper.toDTO(saved);
    }

    public UsuarioResponseDTO reprovarCadastro(Long id) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sem registros nesse id"));

        usuario.reprovar();
        Usuario saved = usuarioRepositoryPort.save(usuario);

        eventPublisher.publishEvent(new ColaboradorAnalisadoEvent(this, saved));

        return mapper.toDTO(saved);
    }
}
