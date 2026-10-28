package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepositoryPort {

    Video save(Video video);

    List<Video> findAll();

    List<Video> findByCursoIdOrderByOrdem(Long cursoId);

    Optional<Video> findById(Long id);

    Optional<Video> findByTitulo(String titulo);

    void delete(Video video);
}
