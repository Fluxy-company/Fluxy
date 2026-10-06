package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.ProgressoAula;
import school.sptech.iefcbackend.domain.port.ProgressoAulaRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.ProgressoAulaJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ProgressoAulaRepositoryAdapter implements ProgressoAulaRepositoryPort {

    private final ProgressoAulaJpaRepository jpaRepository;

    public ProgressoAulaRepositoryAdapter(ProgressoAulaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ProgressoAula> findByUsuarioIdAndVideoId(Long usuarioId, Long videoId) {
        return jpaRepository.findByUsuarioIdAndVideoId(usuarioId, videoId);
    }

    @Override
    public ProgressoAula save(ProgressoAula progressoAula) {
        return jpaRepository.save(progressoAula);
    }

    @Override
    public List<ProgressoAula> findByUsuarioIdAndVideoCursoId(Long usuarioId, Long cursoId) {
        return jpaRepository.findByUsuarioIdAndVideoCursoId(usuarioId, cursoId);
    }
}
