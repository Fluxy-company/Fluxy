package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Curso;

import java.util.List;

public interface CursoJpaRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByTemaId(Long temaId);
}
