package school.sptech.iefcbackend.usecase.inscricao;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.entity.Inscricao;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.CursoRepositoryPort;
import school.sptech.iefcbackend.domain.port.InscricaoRepositoryPort;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;

import java.time.LocalDate;
import java.util.List;

@Service
public class InscricaoService {

    private final InscricaoRepositoryPort inscricaoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final CursoRepositoryPort cursoRepositoryPort;

    public InscricaoService(InscricaoRepositoryPort inscricaoRepositoryPort,
                            UsuarioRepositoryPort usuarioRepositoryPort,
                            CursoRepositoryPort cursoRepositoryPort) {
        this.inscricaoRepositoryPort = inscricaoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.cursoRepositoryPort = cursoRepositoryPort;
    }

    public Inscricao inscrever(Long usuarioId, Long cursoId) {
        if (inscricaoRepositoryPort.existsByUsuarioIdAndCursoId(usuarioId, cursoId)) {
            return inscricaoRepositoryPort.findByUsuarioIdAndCursoId(usuarioId, cursoId).get();
        }

        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Curso curso = cursoRepositoryPort.findById(cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));

        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(usuario);
        inscricao.setCurso(curso);
        inscricao.setDataInscricao(LocalDate.now());

        return inscricaoRepositoryPort.save(inscricao);
    }

    public List<Inscricao> listarPorUsuario(Long usuarioId) {
        return inscricaoRepositoryPort.findByUsuarioId(usuarioId);
    }

    public boolean estaInscrito(Long usuarioId, Long cursoId) {
        return inscricaoRepositoryPort.existsByUsuarioIdAndCursoId(usuarioId, cursoId);
    }

    public void cancelar(Long usuarioId, Long cursoId) {
        Inscricao inscricao = inscricaoRepositoryPort.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada"));
        inscricaoRepositoryPort.delete(inscricao);
    }
}
