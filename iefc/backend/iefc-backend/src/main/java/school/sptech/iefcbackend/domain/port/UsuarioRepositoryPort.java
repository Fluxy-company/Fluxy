package school.sptech.iefcbackend.domain.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findAll();

    Page<Usuario> findAll(Pageable pageable);

    List<Usuario> findAllAdmins();

    List<Usuario> findByStatus(StatusCadastro status);

    Page<Usuario> findByStatus(StatusCadastro status, Pageable pageable);

    Usuario save(Usuario usuario);

    void delete(Usuario usuario);
}
