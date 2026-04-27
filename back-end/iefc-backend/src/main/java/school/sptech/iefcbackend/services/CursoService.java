package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public List<Curso> Listar(){
        return repository.findAll();
    }

    public Curso postar(Curso curso){
        if (curso.getEmpresa() == null || curso.getEmpresa().getId() == null) {
            throw new RecursoNaoEncontradoException("Empresa não informada ou inválida");
        }
        Curso cursoNovo = new Curso();
        cursoNovo.setDescricao(curso.getDescricao());
        cursoNovo.setTitulo(curso.getTitulo());
        cursoNovo.setEmpresa(curso.getEmpresa());
        return repository.save(cursoNovo);
    }

    public Curso buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Não foi possível o curso desejado"));
    }

    public void delete(Long id){
        repository.delete(buscarPorId(id));
    }

    public Curso atualizar(Long id, Curso curso){
        Curso cursoNovo = buscarPorId(id);
        cursoNovo.setEmpresa(curso.getEmpresa());
        cursoNovo.setDescricao(curso.getDescricao());
        cursoNovo.setTitulo(curso.getTitulo());
        return repository.save(cursoNovo);
    }
}
