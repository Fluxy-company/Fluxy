package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.domain.port.VideoRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.VideoJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class VideoRepositoryAdapter implements VideoRepositoryPort {

    private final VideoJpaRepository jpaRepository;

    public VideoRepositoryAdapter(VideoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Video save(Video video) {
        return jpaRepository.save(video);
    }

    @Override
    public List<Video> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Video> findByCursoIdOrderByOrdem(Long cursoId) {
        return jpaRepository.findByCursoIdOrderByOrdem(cursoId);
    }

    @Override
    public Optional<Video> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Video> findByTitulo(String titulo) {
        return jpaRepository.findByTitulo(titulo);
    }

    @Override
    public void delete(Video video) {
        jpaRepository.delete(video);
    }
}
