package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.port.CursoRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.CursoJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class CursoRepositoryAdapter implements CursoRepositoryPort {

    private final CursoJpaRepository jpaRepository;

    public CursoRepositoryAdapter(CursoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Curso> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<Curso> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<Curso> findByTemaId(Long temaId) {
        return jpaRepository.findByTemaId(temaId);
    }

    @Override
    public Curso save(Curso curso) {
        return jpaRepository.save(curso);
    }

    @Override
    public Optional<Curso> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void delete(Curso curso) {
        jpaRepository.delete(curso);
    }
}
