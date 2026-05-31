package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {
}
