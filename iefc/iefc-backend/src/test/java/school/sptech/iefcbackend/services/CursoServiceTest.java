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
import school.sptech.iefcbackend.repository.CursoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository repository;

    @InjectMocks
    private CursoService service;

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
    @DisplayName("Listar deve retornar todos os cursos")
    void listarDeveRetornarTodosOsCursos() {
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setTitulo("React.js");

        when(repository.findAll()).thenReturn(List.of(curso, curso2));

        List<Curso> resultado = service.Listar();

        assertEquals(2, resultado.size());
        assertEquals("Java Spring Boot", resultado.get(0).getTitulo());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Listar deve retornar lista vazia quando não há cursos")
    void listarDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Curso> resultado = service.Listar();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarPorTema deve filtrar por tema")
    void listarPorTemaDeveFiltrar() {
        when(repository.findByTemaId(1L)).thenReturn(List.of(curso));

        List<Curso> resultado = service.listarPorTema(1L);

        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByTemaId(1L);
    }

    @Test
    @DisplayName("buscarPorId deve retornar curso quando encontrado")
    void buscarPorIdDeveRetornarCurso() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));

        Curso resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Java Spring Boot", resultado.getTitulo());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId deve lançar exceção quando não encontrado")
    void buscarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(99L));
        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("postar deve salvar e retornar o curso")
    void postarDeveSalvarCurso() {
        when(repository.save(any(Curso.class))).thenReturn(curso);

        Curso resultado = service.postar(curso);

        assertNotNull(resultado);
        assertEquals("Java Spring Boot", resultado.getTitulo());
        verify(repository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("atualizar deve modificar e salvar o curso")
    void atualizarDeveModificarESalvar() {
        Curso cursoAtualizado = new Curso();
        cursoAtualizado.setTitulo("Java Spring Boot Avançado");
        cursoAtualizado.setDescricao("Curso avançado");
        cursoAtualizado.setInstrutor("Ana Beatriz");
        cursoAtualizado.setVideoId("abc123");

        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        when(repository.save(any(Curso.class))).thenReturn(curso);

        Curso resultado = service.atualizar(1L, cursoAtualizado);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("atualizar deve lançar exceção quando curso não existe")
    void atualizarDeveLancarExcecaoQuandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Curso cursoAtualizado = new Curso();
        cursoAtualizado.setTitulo("Não existe");

        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(99L, cursoAtualizado));
    }

    @Test
    @DisplayName("delete deve remover o curso existente")
    void deleteDeveRemoverCurso() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        doNothing().when(repository).delete(curso);

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository, times(1)).delete(curso);
    }

    @Test
    @DisplayName("delete deve lançar exceção quando curso não existe")
    void deleteDeveLancarExcecaoQuandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.delete(99L));
    }
}
