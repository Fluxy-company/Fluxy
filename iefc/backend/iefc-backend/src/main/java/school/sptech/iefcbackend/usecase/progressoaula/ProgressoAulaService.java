package school.sptech.iefcbackend.usecase.progressoaula;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.ProgressoAula;
import school.sptech.iefcbackend.domain.entity.Usuario;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.ProgressoAulaRepositoryPort;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;
import school.sptech.iefcbackend.domain.port.VideoRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressoAulaService {

    private final ProgressoAulaRepositoryPort progressoAulaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final VideoRepositoryPort videoRepositoryPort;

    public ProgressoAulaService(ProgressoAulaRepositoryPort progressoAulaRepositoryPort,
                                UsuarioRepositoryPort usuarioRepositoryPort,
                                VideoRepositoryPort videoRepositoryPort) {
        this.progressoAulaRepositoryPort = progressoAulaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.videoRepositoryPort = videoRepositoryPort;
    }

    public ProgressoAula marcarConcluida(Long usuarioId, Long videoId, Boolean concluida) {
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vídeo não encontrado"));

        ProgressoAula progresso = progressoAulaRepositoryPort.findByUsuarioIdAndVideoId(usuarioId, videoId)
                .orElseGet(() -> {
                    ProgressoAula novo = new ProgressoAula();
                    novo.setUsuario(usuario);
                    novo.setVideo(video);
                    return novo;
                });

        progresso.setConcluida(concluida);
        progresso.setDataAtualizacao(LocalDateTime.now());

        return progressoAulaRepositoryPort.save(progresso);
    }

    public ProgressoAula salvarAnotacao(Long usuarioId, Long videoId, String anotacao) {
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vídeo não encontrado"));

        ProgressoAula progresso = progressoAulaRepositoryPort.findByUsuarioIdAndVideoId(usuarioId, videoId)
                .orElseGet(() -> {
                    ProgressoAula novo = new ProgressoAula();
                    novo.setUsuario(usuario);
                    novo.setVideo(video);
                    novo.setConcluida(false);
                    return novo;
                });

        progresso.setAnotacao(anotacao);
        progresso.setDataAtualizacao(LocalDateTime.now());

        return progressoAulaRepositoryPort.save(progresso);
    }

    public List<ProgressoAula> listarPorUsuarioECurso(Long usuarioId, Long cursoId) {
        return progressoAulaRepositoryPort.findByUsuarioIdAndVideoCursoId(usuarioId, cursoId);
    }
}
