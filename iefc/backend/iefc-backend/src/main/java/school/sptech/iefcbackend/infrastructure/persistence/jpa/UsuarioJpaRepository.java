package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.enums.Role;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM tb_usuario u JOIN u.roles r WHERE r = 'ADMIN'")
    List<Usuario> findAllAdmins();

    List<Usuario> findByStatus(StatusCadastro status);

    Page<Usuario> findByStatus(StatusCadastro status, Pageable pageable);

    @Query(value = "SELECT u FROM tb_usuario u JOIN u.roles r WHERE r = :role AND u.status = :status",
            countQuery = "SELECT COUNT(u) FROM tb_usuario u JOIN u.roles r WHERE r = :role AND u.status = :status")
    Page<Usuario> findByRoleAndStatus(@Param("role") Role role, @Param("status") StatusCadastro status, Pageable pageable);
}
