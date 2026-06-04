package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.models.Inscricao;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.CursoRepository;
import school.sptech.iefcbackend.repository.InscricaoRepository;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class InscricaoService {

    private final InscricaoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public InscricaoService(InscricaoRepository repository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    public Inscricao inscrever(Long usuarioId, Long cursoId) {
        if (repository.existsByUsuarioIdAndCursoId(usuarioId, cursoId)) {
            return repository.findByUsuarioIdAndCursoId(usuarioId, cursoId).get();
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));

        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(usuario);
        inscricao.setCurso(curso);
        inscricao.setDataInscricao(LocalDate.now());

        return repository.save(inscricao);
    }

    public List<Inscricao> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public boolean estaInscrito(Long usuarioId, Long cursoId) {
        return repository.existsByUsuarioIdAndCursoId(usuarioId, cursoId);
    }

    public void cancelar(Long usuarioId, Long cursoId) {
        Inscricao inscricao = repository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada"));
        repository.delete(inscricao);
    }
}
