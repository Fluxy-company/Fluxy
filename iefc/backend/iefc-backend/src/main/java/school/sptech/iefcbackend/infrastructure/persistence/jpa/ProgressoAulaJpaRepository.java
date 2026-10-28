package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.ProgressoAula;

import java.util.List;
import java.util.Optional;

public interface ProgressoAulaJpaRepository extends JpaRepository<ProgressoAula, Long> {

    List<ProgressoAula> findByUsuarioIdAndVideoCursoId(Long usuarioId, Long cursoId);

    Optional<ProgressoAula> findByUsuarioIdAndVideoId(Long usuarioId, Long videoId);
}
