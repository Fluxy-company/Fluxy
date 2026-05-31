package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Curso;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByTemaId(Long temaId);
}
