package school.sptech.iefcbackend.usecase.evento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.domain.entity.Empresa;
import school.sptech.iefcbackend.domain.entity.Eventos;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.EventoRepositoryPort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepositoryPort repository;

    @InjectMocks
    private EventoService service;

    private Eventos evento;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");

        evento = new Eventos();
        evento.setId(1L);
        evento.setTitulo("Workshop de Java");
        evento.setDescricao("Evento presencial de tecnologia");
        evento.setData(LocalDate.of(2025, 6, 15));
        evento.setStatus(Eventos.StatusEventos.ATIVO);
        evento.setEmpresa(empresa);
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista com eventos")
    void buscarTodosDeveRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Workshop de Java", resultado.get(0).getTitulo());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há eventos")
    void buscarTodosDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Eventos> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarEventoPelaData deve retornar eventos da data informada")
    void buscarEventoPelaDataDeveRetornarLista() {
        LocalDate data = LocalDate.of(2025, 6, 15);
        when(repository.findByData(data)).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarEventoPelaData(data);

        assertEquals(1, resultado.size());
        assertEquals(data, resultado.get(0).getData());
        verify(repository, times(1)).findByData(data);
    }

    @Test
    @DisplayName("buscarEventoPelaData deve lançar exceção quando nenhum evento encontrado")
    void buscarEventoPelaDataDeveLancarExcecao() {
        LocalDate data = LocalDate.of(2099, 1, 1);
        when(repository.findByData(data)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarEventoPelaData(data));
        verify(repository, times(1)).findByData(data);
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve retornar eventos com status correspondente")
    void buscarEventoPeloStatusDeveRetornarLista() {
        when(repository.findByStatus(Eventos.StatusEventos.ATIVO)).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarEventoPeloStatus("ATIVO");

        assertEquals(1, resultado.size());
        assertEquals(Eventos.StatusEventos.ATIVO, resultado.get(0).getStatus());
        verify(repository, times(1)).findByStatus(Eventos.StatusEventos.ATIVO);
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve lançar exceção quando status é inválido")
    void buscarEventoPeloStatusDeveLancarExcecaoStatusInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarEventoPeloStatus("STATUS_INEXISTENTE"));
        verify(repository, never()).findByStatus(any());
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve lançar exceção quando nenhum evento possui o status")
    void buscarEventoPeloStatusDeveLancarExcecaoNenhumEvento() {
        when(repository.findByStatus(Eventos.StatusEventos.FECHADO)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarEventoPeloStatus("FECHADO"));
        verify(repository, times(1)).findByStatus(Eventos.StatusEventos.FECHADO);
    }

    @Test
    @DisplayName("criarEvento deve salvar quando empresa é informada")
    void criarEventoDeveSalvar() {
        when(repository.save(evento)).thenReturn(evento);

        Eventos resultado = service.criarEvento(evento);

        assertNotNull(resultado);
        assertEquals("Workshop de Java", resultado.getTitulo());
        verify(repository, times(1)).save(evento);
    }

    @Test
    @DisplayName("criarEvento deve lançar exceção quando empresa é nula")
    void criarEventoDeveLancarExcecaoEmpresaNula() {
        evento.setEmpresa(null);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.criarEvento(evento));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("criarEvento deve lançar exceção quando id da empresa é nulo")
    void criarEventoDeveLancarExcecaoIdEmpresaNulo() {
        empresa.setId(null);
        evento.setEmpresa(empresa);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.criarEvento(evento));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("atualizarPorId deve atualizar os campos do evento")
    void atualizarPorIdDeveAtualizar() {
        Eventos eventoAtualizado = new Eventos();
        eventoAtualizado.setTitulo("Workshop Atualizado");
        eventoAtualizado.setDescricao("Nova descrição");
        eventoAtualizado.setStatus(Eventos.StatusEventos.FECHADO);
        eventoAtualizado.setData(LocalDate.of(2025, 7, 20));

        when(repository.findById(1L)).thenReturn(Optional.of(evento));
        when(repository.save(any(Eventos.class))).thenReturn(evento);

        Eventos resultado = service.atualizarPorId(1L, eventoAtualizado);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(evento);
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando evento não existe")
    void atualizarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(99L, evento));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deletarPorId deve remover o evento existente")
    void deletarPorIdDeveRemover() {
        when(repository.findById(1L)).thenReturn(Optional.of(evento));
        doNothing().when(repository).delete(evento);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(repository, times(1)).delete(evento);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando evento não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
        verify(repository, never()).delete(any());
    }
}