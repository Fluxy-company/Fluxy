package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Tema;

public interface TemaJpaRepository extends JpaRepository<Tema, Long> {
}
