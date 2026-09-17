package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.iefcbackend.models.StatusCadastro;
import school.sptech.iefcbackend.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM tb_usuario u JOIN u.roles r WHERE r = 'ADMIN'")
    List<Usuario> findAllAdmins();

    List<Usuario> findByStatus(StatusCadastro status);
}
