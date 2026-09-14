package school.sptech.iefcbackend.services;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.ProgressoAula;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.repository.ProgressoAulaRepository;
import school.sptech.iefcbackend.repository.UsuarioRepository;
import school.sptech.iefcbackend.repository.VideoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressoAulaService {

    private final ProgressoAulaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final VideoRepository videoRepository;

    public ProgressoAulaService(ProgressoAulaRepository repository, UsuarioRepository usuarioRepository, VideoRepository videoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.videoRepository = videoRepository;
    }

    public ProgressoAula marcarConcluida(Long usuarioId, Long videoId, Boolean concluida) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vídeo não encontrado"));

        ProgressoAula progresso = repository.findByUsuarioIdAndVideoId(usuarioId, videoId)
                .orElseGet(() -> {
                    ProgressoAula novo = new ProgressoAula();
                    novo.setUsuario(usuario);
                    novo.setVideo(video);
                    return novo;
                });

        progresso.setConcluida(concluida);
        progresso.setDataAtualizacao(LocalDateTime.now());

        return repository.save(progresso);
    }

    public ProgressoAula salvarAnotacao(Long usuarioId, Long videoId, String anotacao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vídeo não encontrado"));

        ProgressoAula progresso = repository.findByUsuarioIdAndVideoId(usuarioId, videoId)
                .orElseGet(() -> {
                    ProgressoAula novo = new ProgressoAula();
                    novo.setUsuario(usuario);
                    novo.setVideo(video);
                    novo.setConcluida(false);
                    return novo;
                });

        progresso.setAnotacao(anotacao);
        progresso.setDataAtualizacao(LocalDateTime.now());

        return repository.save(progresso);
    }

    public List<ProgressoAula> listarPorUsuarioECurso(Long usuarioId, Long cursoId) {
        return repository.findByUsuarioIdAndVideoCursoId(usuarioId, cursoId);
    }
}
