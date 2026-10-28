package school.sptech.iefcbackend.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import school.sptech.iefcbackend.domain.entity.Eventos;
import school.sptech.iefcbackend.domain.port.EventoRepositoryPort;
import school.sptech.iefcbackend.infrastructure.persistence.jpa.EventoJpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class EventoRepositoryAdapter implements EventoRepositoryPort {

    private final EventoJpaRepository jpaRepository;

    public EventoRepositoryAdapter(EventoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Eventos save(Eventos evento) {
        return jpaRepository.save(evento);
    }

    @Override
    public List<Eventos> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Eventos> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Eventos> findByData(LocalDate data) {
        return jpaRepository.findByData(data);
    }

    @Override
    public List<Eventos> findByStatus(Eventos.StatusEventos status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public void delete(Eventos evento) {
        jpaRepository.delete(evento);
    }
}
