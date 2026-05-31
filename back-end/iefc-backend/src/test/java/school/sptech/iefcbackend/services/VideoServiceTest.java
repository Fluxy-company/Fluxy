package school.sptech.iefcbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.repository.VideoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository repository;

    @InjectMocks
    private VideoService service;

    private Video video;
    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setTitulo("Java Spring Boot");

        video = new Video();
        video.setId(1L);
        video.setTitulo("Introdução ao Spring Boot");
        video.setUrl("https://www.youtube.com/watch?v=wlYvA2b1BWI");
        video.setVideoId("wlYvA2b1BWI");
        video.setDuracao("15:30");
        video.setModulo("Introdução");
        video.setOrdem(1);
        video.setCurso(curso);
    }

    @Test
    @DisplayName("buscarTodos deve retornar todos os videos")
    void buscarTodosDeveRetornarTodosOsVideos() {
        Video video2 = new Video();
        video2.setId(2L);
        video2.setTitulo("Configurando Projeto");

        when(repository.findAll()).thenReturn(List.of(video, video2));

        List<Video> resultado = service.buscarTodos();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia")
    void buscarTodosDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Video> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorCursoId deve retornar videos do curso ordenados")
    void buscarPorCursoIdDeveRetornarVideosOrdenados() {
        Video video2 = new Video();
        video2.setId(2L);
        video2.setTitulo("Configurando Projeto");
        video2.setOrdem(2);
        video2.setCurso(curso);

        when(repository.findByCursoIdOrderByOrdem(1L)).thenReturn(List.of(video, video2));

        List<Video> resultado = service.buscarPorCursoId(1L);

        assertEquals(2, resultado.size());
        assertEquals("Introdução ao Spring Boot", resultado.get(0).getTitulo());
        verify(repository, times(1)).findByCursoIdOrderByOrdem(1L);
    }

    @Test
    @DisplayName("buscarPorCursoId deve retornar lista vazia quando curso não tem videos")
    void buscarPorCursoIdDeveRetornarVazioQuandoSemVideos() {
        when(repository.findByCursoIdOrderByOrdem(99L)).thenReturn(Collections.emptyList());

        List<Video> resultado = service.buscarPorCursoId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("salvarVideo deve persistir o video")
    void salvarVideoDevePersistir() {
        when(repository.save(any(Video.class))).thenReturn(video);

        assertDoesNotThrow(() -> service.salvarVideo(video));
        verify(repository, times(1)).save(video);
    }

    @Test
    @DisplayName("buscarVideoPeloTitulo deve retornar video quando encontrado")
    void buscarVideoPeloTituloDeveRetornar() {
        when(repository.findByTitulo("Introdução ao Spring Boot")).thenReturn(Optional.of(video));

        Video resultado = service.buscarVideoPeloTitulo("Introdução ao Spring Boot");

        assertNotNull(resultado);
        assertEquals("wlYvA2b1BWI", resultado.getVideoId());
    }

    @Test
    @DisplayName("buscarVideoPeloTitulo deve lançar exceção quando não encontrado")
    void buscarVideoPeloTituloDeveLancarExcecao() {
        when(repository.findByTitulo("Inexistente")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarVideoPeloTitulo("Inexistente"));
    }

    @Test
    @DisplayName("atualizarPeloId deve modificar e salvar o video")
    void atualizarPeloIdDeveModificarESalvar() {
        Video videoAtualizado = new Video();
        videoAtualizado.setTitulo("Título atualizado");
        videoAtualizado.setUrl("https://youtube.com/watch?v=abc123");
        videoAtualizado.setVideoId("abc123");
        videoAtualizado.setDuracao("20:00");
        videoAtualizado.setModulo("Módulo 1");
        videoAtualizado.setOrdem(2);
        videoAtualizado.setCurso(curso);

        when(repository.findById(1L)).thenReturn(Optional.of(video));
        when(repository.save(any(Video.class))).thenReturn(video);

        Video resultado = service.atualizarPeloId(1L, videoAtualizado);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Video.class));
    }

    @Test
    @DisplayName("atualizarPeloId deve lançar exceção quando não encontrado")
    void atualizarPeloIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Video videoAtualizado = new Video();
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPeloId(99L, videoAtualizado));
    }

    @Test
    @DisplayName("deletarPorId deve remover o video existente")
    void deletarPorIdDeveRemover() {
        when(repository.findById(1L)).thenReturn(Optional.of(video));
        doNothing().when(repository).delete(video);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(repository, times(1)).delete(video);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando não encontrado")
    void deletarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
    }
}
