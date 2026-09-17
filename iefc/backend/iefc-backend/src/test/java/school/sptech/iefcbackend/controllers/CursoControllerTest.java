package school.sptech.iefcbackend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.services.CursoService;
import school.sptech.iefcbackend.services.VideoService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    @Mock
    private VideoService videoService;

    @InjectMocks
    private CursoController controller;

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setTitulo("Java Spring Boot");
        curso.setDescricao("API REST completa");
        curso.setInstrutor("Ana Beatriz");
        curso.setVideoId("wlYvA2b1BWI");
    }

    @Test
    @DisplayName("GET /cursos deve retornar 200 com lista de cursos")
    void listarDeveRetornar200ComCursos() {
        when(cursoService.Listar()).thenReturn(List.of(curso));

        ResponseEntity<List<Curso>> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Java Spring Boot", response.getBody().get(0).getTitulo());
    }

    @Test
    @DisplayName("GET /cursos deve retornar 200 com lista vazia")
    void listarDeveRetornar200ComListaVazia() {
        when(cursoService.Listar()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Curso>> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /cursos/{id} deve retornar 200 com curso encontrado")
    void buscarPorIdDeveRetornar200() {
        when(cursoService.buscarPorId(1L)).thenReturn(curso);

        ResponseEntity<Curso> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Java Spring Boot", response.getBody().getTitulo());
    }

    @Test
    @DisplayName("GET /cursos/{id} deve propagar exceção quando não encontrado")
    void buscarPorIdDevePropagarExcecao() {
        when(cursoService.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Não encontrado"));

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.buscarPorId(99L));
    }

    @Test
    @DisplayName("GET /cursos/tema/{temaId} deve retornar cursos filtrados")
    void buscarPorTemaDeveRetornarFiltrados() {
        when(cursoService.listarPorTema(1L)).thenReturn(List.of(curso));

        ResponseEntity<List<Curso>> response = controller.buscarPorTema(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /cursos/{id}/videos deve retornar videos do curso")
    void buscarVideosPorCursoDeveRetornar() {
        Video video = new Video();
        video.setId(1L);
        video.setTitulo("Aula 1");
        video.setVideoId("abc123");

        when(videoService.buscarPorCursoId(1L)).thenReturn(List.of(video));

        ResponseEntity<List<Video>> response = controller.buscarVideosPorCurso(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Aula 1", response.getBody().get(0).getTitulo());
    }

    @Test
    @DisplayName("POST /cursos deve retornar 201 com curso criado")
    void postarDeveRetornar201() {
        when(cursoService.postar(any(Curso.class))).thenReturn(curso);

        ResponseEntity<Curso> response = controller.postar(curso);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /cursos/{id} deve retornar 200 com curso atualizado")
    void atualizarDeveRetornar200() {
        when(cursoService.atualizar(eq(1L), any(Curso.class))).thenReturn(curso);

        ResponseEntity<Curso> response = controller.atualizar(1L, curso);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /cursos/{id} deve retornar 204")
    void deletarDeveRetornar204() {
        doNothing().when(cursoService).delete(1L);

        ResponseEntity<?> response = controller.deletar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cursoService, times(1)).delete(1L);
    }
}
