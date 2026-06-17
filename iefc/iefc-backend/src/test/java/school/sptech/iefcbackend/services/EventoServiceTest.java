package school.sptech.iefcbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Empresa;
import school.sptech.iefcbackend.models.Eventos;
import school.sptech.iefcbackend.repository.EventoRepository;

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
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService service;

    private Eventos evento;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");
        empresa.setCnpj("12.345.678/0001-99");
        empresa.setTelefone("11999999999");

        evento = new Eventos();
        evento.setId(1L);
        evento.setTitulo("Hackathon 2025");
        evento.setDescricao("Maratona de programação");
        evento.setData(LocalDate.of(2025, 9, 20));
        evento.setStatus(Eventos.StatusEventos.ATIVO);
        evento.setEmpresa(empresa);
    }

    @Test
    @DisplayName("criarEvento deve salvar e retornar o evento")
    void criarEventoDeveSalvarERetornar() {
        when(eventoRepository.save(any(Eventos.class))).thenReturn(evento);

        Eventos resultado = service.criarEvento(evento);

        assertNotNull(resultado);
        assertEquals("Hackathon 2025", resultado.getTitulo());
        verify(eventoRepository, times(1)).save(any(Eventos.class));
    }

    @Test
    @DisplayName("criarEvento deve lançar exceção quando empresa não informada")
    void criarEventoDeveLancarExcecaoSemEmpresa() {
        Eventos eventoSemEmpresa = new Eventos();
        eventoSemEmpresa.setTitulo("Evento sem empresa");

        assertThrows(RecursoNaoEncontradoException.class, () -> service.criarEvento(eventoSemEmpresa));
        verify(eventoRepository, never()).save(any());
    }

    @Test
    @DisplayName("criarEvento deve lançar exceção quando id da empresa é nulo")
    void criarEventoDeveLancarExcecaoIdEmpresaNulo() {
        Empresa empresaSemId = new Empresa();
        Eventos eventoComEmpresaSemId = new Eventos();
        eventoComEmpresaSemId.setEmpresa(empresaSemId);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.criarEvento(eventoComEmpresaSemId));
    }

    @Test
    @DisplayName("buscarTodos deve retornar todos os eventos")
    void buscarTodosDeveRetornarTodosEventos() {
        when(eventoRepository.findAll()).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há eventos")
    void buscarTodosDeveRetornarListaVazia() {
        when(eventoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Eventos> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarEventoPelaData deve retornar eventos na data informada")
    void buscarEventoPelaDataDeveRetornarEventos() {
        LocalDate data = LocalDate.of(2025, 9, 20);
        when(eventoRepository.findByData(data)).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarEventoPelaData(data);

        assertEquals(1, resultado.size());
        verify(eventoRepository, times(1)).findByData(data);
    }

    @Test
    @DisplayName("buscarEventoPelaData deve lançar exceção quando não há eventos na data")
    void buscarEventoPelaDataDeveLancarExcecao() {
        LocalDate data = LocalDate.of(2020, 1, 1);
        when(eventoRepository.findByData(data)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarEventoPelaData(data));
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve retornar eventos com status ATIVO")
    void buscarEventoPeloStatusDeveRetornarEventos() {
        when(eventoRepository.findByStatus(Eventos.StatusEventos.ATIVO)).thenReturn(List.of(evento));

        List<Eventos> resultado = service.buscarEventoPeloStatus("ATIVO");

        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve lançar exceção com status inválido")
    void buscarEventoPeloStatusDeveLancarExcecaoStatusInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarEventoPeloStatus("INVALIDO"));
    }

    @Test
    @DisplayName("buscarEventoPeloStatus deve lançar exceção quando lista vazia")
    void buscarEventoPeloStatusDeveLancarExcecaoListaVazia() {
        when(eventoRepository.findByStatus(Eventos.StatusEventos.CANCELADO)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarEventoPeloStatus("CANCELADO"));
    }

    @Test
    @DisplayName("atualizarPorId deve modificar e salvar o evento")
    void atualizarPorIdDeveModificarESalvar() {
        Eventos eventoAtualizado = new Eventos();
        eventoAtualizado.setTitulo("Hackathon 2026");
        eventoAtualizado.setDescricao("Nova edição");
        eventoAtualizado.setStatus(Eventos.StatusEventos.EM_ANALISE);
        eventoAtualizado.setData(LocalDate.of(2026, 3, 10));

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(eventoRepository.save(any(Eventos.class))).thenAnswer(inv -> inv.getArgument(0));

        Eventos resultado = service.atualizarPorId(1L, eventoAtualizado);

        assertEquals("Hackathon 2026", resultado.getTitulo());
        verify(eventoRepository, times(1)).save(any(Eventos.class));
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando evento não existe")
    void atualizarPorIdDeveLancarExcecao() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(99L, new Eventos()));
    }

    @Test
    @DisplayName("deletarPorId deve remover o evento existente")
    void deletarPorIdDeveRemoverEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        doNothing().when(eventoRepository).delete(evento);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(eventoRepository, times(1)).delete(evento);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando evento não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletarPorId(99L));
    }
}