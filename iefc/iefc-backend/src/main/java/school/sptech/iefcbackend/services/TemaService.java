package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Tema;
import school.sptech.iefcbackend.repository.TemaRepository;

import java.util.List;

@Service
public class TemaService {

    private final TemaRepository repository;

    public TemaService(TemaRepository repository) {
        this.repository = repository;
    }

    public List<Tema> listar() {
        return repository.findAll();
    }

    public Tema buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tema não encontrado"));
    }

    public Tema criar(Tema tema) {
        return repository.save(tema);
    }

    public Tema atualizar(Long id, Tema tema) {
        Tema existente = buscarPorId(id);
        existente.setNome(tema.getNome());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        repository.delete(buscarPorId(id));
    }
}
