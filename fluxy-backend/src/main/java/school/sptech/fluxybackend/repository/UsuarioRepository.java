package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
