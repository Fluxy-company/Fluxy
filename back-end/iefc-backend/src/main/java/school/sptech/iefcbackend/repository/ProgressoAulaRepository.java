package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.ProgressoAula;

import java.util.List;
import java.util.Optional;

public interface ProgressoAulaRepository extends JpaRepository<ProgressoAula, Long> {

    List<ProgressoAula> findByUsuarioIdAndVideoCursoId(Long usuarioId, Long cursoId);

    Optional<ProgressoAula> findByUsuarioIdAndVideoId(Long usuarioId, Long videoId);
}
