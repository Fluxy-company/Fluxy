package school.sptech.iefcbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.repository.VideoRepository;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    VideoRepository repository;

    public void salvarVideo(Video video){
        repository.save(video);
    }

    public List<Video> buscarTodos(){
        return repository.findAll();
    }

    public List<Video> buscarPorCursoId(Long cursoId){
        return repository.findByCursoIdOrderByOrdem(cursoId);
    }

    public Video buscarVideoPeloTitulo(String titulo){
        return repository.findByTitulo(titulo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse titulo"));
    }

    public Video atualizarPeloId(Long id, Video video){
        Video videoEntity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse id."));

        videoEntity.setTitulo(video.getTitulo());
        videoEntity.setUrl(video.getUrl());
        videoEntity.setVideoId(video.getVideoId());
        videoEntity.setDuracao(video.getDuracao());
        videoEntity.setModulo(video.getModulo());
        videoEntity.setOrdem(video.getOrdem());
        videoEntity.setCurso(video.getCurso());

        return repository.save(videoEntity);
    }

    public void deletarPorId(Long id){
        Video video = repository.findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse id"));
        repository.delete(video);
    }
}
