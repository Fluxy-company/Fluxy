package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.UsuarioJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<Usuario> findAllAdmins() {
        return jpaRepository.findAllAdmins();
    }

    @Override
    public List<Usuario> findByStatus(StatusCadastro status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public Page<Usuario> findByStatus(StatusCadastro status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return jpaRepository.save(usuario);
    }

    @Override
    public void delete(Usuario usuario) {
        jpaRepository.delete(usuario);
    }
}
