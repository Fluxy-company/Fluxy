package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Inscricao;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByUsuarioId(Long usuarioId);

    Optional<Inscricao> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);

    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}
