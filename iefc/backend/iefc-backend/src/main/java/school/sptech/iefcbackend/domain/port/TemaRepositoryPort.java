package school.sptech.iefcbackend.domain.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import school.sptech.iefcbackend.domain.entity.Tema;

import java.util.List;
import java.util.Optional;

public interface TemaRepositoryPort {

    Page<Tema> findAll(Pageable pageable);

    Optional<Tema> findById(Long id);

    Tema save(Tema tema);

    void delete(Tema tema);
}
