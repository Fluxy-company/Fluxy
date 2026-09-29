package school.sptech.iefcbackend.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.domain.entity.Eventos;

import java.time.LocalDate;
import java.util.List;

public interface EventoJpaRepository extends JpaRepository<Eventos, Long> {

    List<Eventos> findByData(LocalDate data);

    List<Eventos> findByStatus(Eventos.StatusEventos status);
}
