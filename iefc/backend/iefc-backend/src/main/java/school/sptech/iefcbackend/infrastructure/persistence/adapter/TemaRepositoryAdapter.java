package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Tema;
import school.sptech.iefcbackend.domain.port.TemaRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.TemaJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class TemaRepositoryAdapter implements TemaRepositoryPort {

    private final TemaJpaRepository jpaRepository;

    public TemaRepositoryAdapter(TemaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<Tema> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Tema> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Tema save(Tema tema) {
        return jpaRepository.save(tema);
    }

    @Override
    public void delete(Tema tema) {
        jpaRepository.delete(tema);
    }
}
