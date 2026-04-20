package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
