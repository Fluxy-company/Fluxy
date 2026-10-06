package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Inscricao;
import school.sptech.iefcbackend.domain.port.InscricaoRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.InscricaoJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class InscricaoRepositoryAdapter implements InscricaoRepositoryPort {

    private final InscricaoJpaRepository jpaRepository;

    public InscricaoRepositoryAdapter(InscricaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId) {
        return jpaRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId);
    }

    @Override
    public Optional<Inscricao> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId) {
        return jpaRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId);
    }

    @Override
    public Inscricao save(Inscricao inscricao) {
        return jpaRepository.save(inscricao);
    }

    @Override
    public List<Inscricao> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public void delete(Inscricao inscricao) {
        jpaRepository.delete(inscricao);
    }
}
