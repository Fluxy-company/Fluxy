package school.sptech.iefcbackend.usecase.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.CursoRepositoryPort;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepositoryPort cursoRepositoryPort;

    public CursoService(CursoRepositoryPort cursoRepositoryPort) {
        this.cursoRepositoryPort = cursoRepositoryPort;
    }

    public List<Curso> Listar() {
        return cursoRepositoryPort.findAll();
    }

    public Page<Curso> listarPaginado(Pageable pageable) {
        return cursoRepositoryPort.findAll(pageable);
    }

    public List<Curso> listarPorTema(Long temaId) {
        return cursoRepositoryPort.findByTemaId(temaId);
    }

    public Curso postar(Curso curso) {
        Curso cursoNovo = new Curso();
        cursoNovo.setDescricao(curso.getDescricao());
        cursoNovo.setTitulo(curso.getTitulo());
        cursoNovo.setInstrutor(curso.getInstrutor());
        cursoNovo.setVideoId(curso.getVideoId());
        cursoNovo.setTema(curso.getTema());
        cursoNovo.setEmpresa(curso.getEmpresa());
        return cursoRepositoryPort.save(cursoNovo);
    }

    public Curso buscarPorId(Long id) {
        return cursoRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não foi possível encontrar o curso desejado"));
    }

    public void delete(Long id) {
        cursoRepositoryPort.delete(buscarPorId(id));
    }

    public Curso atualizar(Long id, Curso curso) {
        Curso cursoNovo = buscarPorId(id);
        cursoNovo.setEmpresa(curso.getEmpresa());
        cursoNovo.setDescricao(curso.getDescricao());
        cursoNovo.setTitulo(curso.getTitulo());
        cursoNovo.setInstrutor(curso.getInstrutor());
        cursoNovo.setVideoId(curso.getVideoId());
        cursoNovo.setTema(curso.getTema());
        return cursoRepositoryPort.save(cursoNovo);
    }
}
