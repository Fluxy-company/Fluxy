package school.sptech.iefcbackend.domain.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import school.sptech.iefcbackend.domain.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoRepositoryPort {

    List<Curso> findAll();

    Page<Curso> findAll(Pageable pageable);

    List<Curso> findByTemaId(Long temaId);

    Curso save(Curso curso);

    Optional<Curso> findById(Long id);

    void delete(Curso curso);
}
