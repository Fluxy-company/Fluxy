package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.Inscricao;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepositoryPort {

    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);

    Optional<Inscricao> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);

    Inscricao save(Inscricao inscricao);

    List<Inscricao> findByUsuarioId(Long usuarioId);

    void delete(Inscricao inscricao);
}
