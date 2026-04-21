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

    public Video buscarVideoPeloTitulo(String titulo){
        return repository.findByTitulo(titulo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum evento enconrado com esse titulo"));
    }

    public Video atualizarPeloId(Long id, Video video){
        Video videoEntity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum video encontrado com esse id."));

        videoEntity.setTitulo(video.getTitulo());
        videoEntity.setUrl(video.getUrl());
        videoEntity.setCursos(video.getCursos());

        return repository.save(videoEntity);
    }

    public void deletarPorId(Long id){
        Video video = repository.findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException("Nenhum event encontrado com esse id"));
        repository.delete(video);
    }
}
