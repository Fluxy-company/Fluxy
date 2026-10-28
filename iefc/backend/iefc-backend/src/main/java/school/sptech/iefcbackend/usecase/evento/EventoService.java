package school.sptech.iefcbackend.usecase.evento;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Eventos;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.EventoRepositoryPort;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepositoryPort eventoRepositoryPort;

    public EventoService(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Eventos criarEvento(Eventos evento) {
        if (evento.getEmpresa() == null || evento.getEmpresa().getId() == null) {
            throw new RecursoNaoEncontradoException("Empresa não informada ou inválida");
        }
        return eventoRepositoryPort.save(evento);
    }

    public List<Eventos> buscarTodos() {
        return eventoRepositoryPort.findAll();
    }

    public List<Eventos> buscarEventoPelaData(LocalDate data) {
        List<Eventos> eventos = eventoRepositoryPort.findByData(data);

        if (eventos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum evento nessa data");
        }
        return eventos;
    }

    public List<Eventos> buscarEventoPeloStatus(String status) {
        Eventos.StatusEventos statusEnum;
        try {
            statusEnum = Eventos.StatusEventos.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        List<Eventos> eventos = eventoRepositoryPort.findByStatus(statusEnum);
        if (eventos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum evetno com esse status");
        }
        return eventos;
    }

    public Eventos atualizarPorId(Long id, Eventos evento) {
        Eventos eventoEntity = eventoRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("nenhum evento encontrado com esse id"));
        eventoEntity.setTitulo(evento.getTitulo());
        eventoEntity.setDescricao(evento.getDescricao());
        eventoEntity.setStatus(evento.getStatus());
        eventoEntity.setData(evento.getData());

        return eventoRepositoryPort.save(eventoEntity);
    }

    public void deletarPorId(Long id) {
        Eventos evento = eventoRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum event encontrado com esse id"));
        eventoRepositoryPort.delete(evento);
    }
}
