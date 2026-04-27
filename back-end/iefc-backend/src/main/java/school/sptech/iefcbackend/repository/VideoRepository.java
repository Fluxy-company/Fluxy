package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByTitulo(String titulo);
}
