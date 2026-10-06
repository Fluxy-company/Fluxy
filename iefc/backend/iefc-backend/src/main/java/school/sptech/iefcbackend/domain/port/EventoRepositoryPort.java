package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.domain.entity.Eventos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventoRepositoryPort {

    Eventos save(Eventos evento);

    List<Eventos> findAll();

    Optional<Eventos> findById(Long id);

    List<Eventos> findByData(LocalDate data);

    List<Eventos> findByStatus(Eventos.StatusEventos status);

    void delete(Eventos evento);
}
