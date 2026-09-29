package school.sptech.iefcbackend.usecase.tema;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Tema;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.TemaRepositoryPort;

import java.util.List;

@Service
public class TemaService {

    private final TemaRepositoryPort temaRepositoryPort;

    public TemaService(TemaRepositoryPort temaRepositoryPort) {
        this.temaRepositoryPort = temaRepositoryPort;
    }

    public List<Tema> listar() {
        return temaRepositoryPort.findAll();
    }

    public Page<Tema> listarPaginado(Pageable pageable) {
        return temaRepositoryPort.findAll(pageable);
    }

    public Tema buscarPorId(Long id) {
        return temaRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tema não encontrado"));
    }

    public Tema criar(Tema tema) {
        return temaRepositoryPort.save(tema);
    }

    public Tema atualizar(Long id, Tema tema) {
        Tema existente = buscarPorId(id);
        existente.setNome(tema.getNome());
        return temaRepositoryPort.save(existente);
    }

    public void deletar(Long id) {
        temaRepositoryPort.delete(buscarPorId(id));
    }
}
