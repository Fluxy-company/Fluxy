package school.sptech.fluxybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.fluxybackend.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNome(String nome);
}

