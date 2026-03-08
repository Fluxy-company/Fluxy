package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
