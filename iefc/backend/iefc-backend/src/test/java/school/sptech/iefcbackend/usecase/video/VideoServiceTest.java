package school.sptech.iefcbackend.usecase.video;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.VideoRepositoryPort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepositoryPort repository;

    @InjectMocks
    private VideoService service;

    private Video video;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setTitulo("Curso Spring Boot");

        video = new Video();
        video.setId(1L);
        video.setTitulo("Introdução ao Spring Boot");
        video.setUrl("https://youtube.com/watch?v=abc");
        video.setVideoId("abc123xyz");
        video.setDuracao("10:30");
        video.setModulo("Módulo 1");
        video.setOrdem(1);
        video.setCurso(curso);
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista com vídeos")
    void buscarTodosDeveRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(video));

        List<Video> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Introdução ao Spring Boot", resultado.get(0).getTitulo());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há vídeos")
    void buscarTodosDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Video> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorCursoId deve retornar vídeos ordenados por ordem")
    void buscarPorCursoIdDeveRetornarListaOrdenada() {
        Video video2 = new Video();
        video2.setId(2L);
        video2.setTitulo("Configurando o Ambiente");
        video2.setOrdem(2);

        when(repository.findByCursoIdOrderByOrdem(1L)).thenReturn(List.of(video, video2));

        List<Video> resultado = service.buscarPorCursoId(1L);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getOrdem());
        assertEquals(2, resultado.get(1).getOrdem());
        verify(repository, times(1)).findByCursoIdOrderByOrdem(1L);
    }

    @Test
    @DisplayName("buscarVideoPeloTitulo deve retornar vídeo quando título existe")
    void buscarVideoPeloTituloDeveRetornarVideo() {
        when(repository.findByTitulo("Introdução ao Spring Boot")).thenReturn(Optional.of(video));

        Video resultado = service.buscarVideoPeloTitulo("Introdução ao Spring Boot");

        assertNotNull(resultado);
        assertEquals("Introdução ao Spring Boot", resultado.getTitulo());
        verify(repository, times(1)).findByTitulo("Introdução ao Spring Boot");
    }

    @Test
    @DisplayName("buscarVideoPeloTitulo deve lançar exceção quando título não existe")
    void buscarVideoPeloTituloDeveLancarExcecao() {
        when(repository.findByTitulo("Inexistente")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarVideoPeloTitulo("Inexistente"));
        verify(repository, times(1)).findByTitulo("Inexistente");
    }

    @Test
    @DisplayName("salvarVideo deve salvar e retornar o vídeo")
    void salvarVideoDeveSalvar() {
        when(repository.save(video)).thenReturn(video);

        Video resultado = service.salvarVideo(video);

        assertNotNull(resultado);
        assertEquals("Introdução ao Spring Boot", resultado.getTitulo());
        verify(repository, times(1)).save(video);
    }

    @Test
    @DisplayName("atualizarPeloId deve atualizar os campos do vídeo")
    void atualizarPeloIdDeveAtualizar() {
        Video videoAtualizado = new Video();
        videoAtualizado.setTitulo("Título Atualizado");
        videoAtualizado.setUrl("https://youtube.com/watch?v=novo");
        videoAtualizado.setVideoId("novoid");
        videoAtualizado.setDuracao("15:00");
        videoAtualizado.setModulo("Módulo 2");
        videoAtualizado.setOrdem(2);
        videoAtualizado.setCurso(curso);

        when(repository.findById(1L)).thenReturn(Optional.of(video));
        when(repository.save(any(Video.class))).thenReturn(video);

        Video resultado = service.atualizarPeloId(1L, videoAtualizado);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(video);
    }

    @Test
    @DisplayName("atualizarPeloId deve lançar exceção quando vídeo não existe")
    void atualizarPeloIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPeloId(99L, video));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deletarPorId deve remover o vídeo existente")
    void deletarPorIdDeveRemover() {
        when(repository.findById(1L)).thenReturn(Optional.of(video));
        doNothing().when(repository).delete(video);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(repository, times(1)).delete(video);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando vídeo não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
        verify(repository, never()).delete(any());
    }
}
