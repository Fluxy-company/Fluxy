package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Eventos;
import school.sptech.iefcbackend.repository.EventoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Eventos criarEvento(Eventos evento){
        if (evento.getEmpresa() == null || evento.getEmpresa().getId() == null) {
            throw new RecursoNaoEncontradoException("Empresa não informada ou inválida");
        }
        return eventoRepository.save(evento);
    }

    public List<Eventos> buscarTodos(){
        return eventoRepository.findAll();
    }

    public List<Eventos> buscarEventoPelaData(LocalDate data){
        List<Eventos> eventos = eventoRepository.findByData(data);

        if(eventos.isEmpty()){
            throw  new RecursoNaoEncontradoException("Nenhum evento nessa data");
        }
        return eventos;
    }

    public List<Eventos> buscarEventoPeloStatus(String status){
        Eventos.StatusEventos statusEnum;
        try{
            statusEnum = Eventos.StatusEventos.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        List<Eventos> eventos = eventoRepository.findByStatus(statusEnum);
        if(eventos.isEmpty()){
            throw  new RecursoNaoEncontradoException("Nenhum evetno com esse status");
        }
        return eventos;
    }



    public Eventos atualizarPorId(Long id, Eventos evento){
        Eventos eventoEntity = eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("nenhum evento encontrado com esse id"));
        eventoEntity.setTitulo(evento.getTitulo());
        eventoEntity.setDescricao(evento.getDescricao());
        eventoEntity.setStatus(evento.getStatus());
        eventoEntity.setData(evento.getData());

        return eventoRepository.save(eventoEntity);
    }

    public void deletarPorId(Long id){
        Eventos evento = eventoRepository.findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException("Nenhum event encontrado com esse id"));
        eventoRepository.delete(evento);
    }
}
