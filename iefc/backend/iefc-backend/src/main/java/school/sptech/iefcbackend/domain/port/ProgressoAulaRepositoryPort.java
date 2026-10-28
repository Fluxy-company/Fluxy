package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.ProgressoAula;

import java.util.List;
import java.util.Optional;

public interface ProgressoAulaRepositoryPort {

    Optional<ProgressoAula> findByUsuarioIdAndVideoId(Long usuarioId, Long videoId);

    ProgressoAula save(ProgressoAula progressoAula);

    List<ProgressoAula> findByUsuarioIdAndVideoCursoId(Long usuarioId, Long cursoId);
}
