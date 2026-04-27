package school.sptech.iefcbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.iefcbackend.models.Eventos;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Eventos, Long> {

    List<Eventos> findByData(LocalDate data);

    List<Eventos> findByStatus(String status);
}
