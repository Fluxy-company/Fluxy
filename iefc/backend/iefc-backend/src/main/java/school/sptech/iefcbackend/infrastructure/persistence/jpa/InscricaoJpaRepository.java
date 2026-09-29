package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Inscricao;

import java.util.List;
import java.util.Optional;

public interface InscricaoJpaRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByUsuarioId(Long usuarioId);

    Optional<Inscricao> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);

    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}
