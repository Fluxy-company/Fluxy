package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNome(String nome);
}

