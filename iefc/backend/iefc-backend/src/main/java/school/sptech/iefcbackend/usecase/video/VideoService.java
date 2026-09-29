package school.sptech.iefcbackend.usecase.video;

import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.VideoRepositoryPort;

import java.util.List;

@Service
public class VideoService {

    private final VideoRepositoryPort videoRepositoryPort;

    public VideoService(VideoRepositoryPort videoRepositoryPort) {
        this.videoRepositoryPort = videoRepositoryPort;
    }

    public Video salvarVideo(Video video) {
        return videoRepositoryPort.save(video);
    }

    public List<Video> buscarTodos() {
        return videoRepositoryPort.findAll();
    }

    public List<Video> buscarPorCursoId(Long cursoId) {
        return videoRepositoryPort.findByCursoIdOrderByOrdem(cursoId);
    }

    public Video buscarVideoPeloTitulo(String titulo) {
        return videoRepositoryPort.findByTitulo(titulo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse titulo"));
    }

    public Video atualizarPeloId(Long id, Video video) {
        Video videoEntity = videoRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse id."));

        videoEntity.setTitulo(video.getTitulo());
        videoEntity.setUrl(video.getUrl());
        videoEntity.setVideoId(video.getVideoId());
        videoEntity.setDuracao(video.getDuracao());
        videoEntity.setModulo(video.getModulo());
        videoEntity.setOrdem(video.getOrdem());
        videoEntity.setCurso(video.getCurso());

        return videoRepositoryPort.save(videoEntity);
    }

    public void deletarPorId(Long id) {
        Video video = videoRepositoryPort.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse id"));
        videoRepositoryPort.delete(video);
    }
}
