package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Video;

import java.util.List;
import java.util.Optional;

public interface VideoJpaRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByTitulo(String titulo);

    List<Video> findByCursoIdOrderByOrdem(Long cursoId);
}
