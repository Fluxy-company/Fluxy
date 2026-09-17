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
import school.sptech.iefcbackend.models.Empresa;
import school.sptech.iefcbackend.models.Eventos;
import school.sptech.iefcbackend.services.EventoService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    @Mock
    private EventoService eventoService;

    @InjectMocks
    private EventoController controller;

    private Eventos evento;

    @BeforeEach
    void setUp() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);

        evento = new Eventos();
        evento.setId(1L);
        evento.setTitulo("Hackathon 2025");
        evento.setDescricao("Maratona de programação");
        evento.setData(LocalDate.of(2025, 9, 20));
        evento.setStatus(Eventos.StatusEventos.ATIVO);
        evento.setEmpresa(empresa);
    }

    @Test
    @DisplayName("POST /eventos deve retornar 201 com evento criado")
    void criarEventoDeveRetornar201() {
        when(eventoService.criarEvento(any(Eventos.class))).thenReturn(evento);

        ResponseEntity<Eventos> response = controller.criarEvento(evento);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hackathon 2025", response.getBody().getTitulo());
    }

    @Test
    @DisplayName("POST /eventos deve propagar exceção quando empresa não informada")
    void criarEventoDevePropagarExcecaoSemEmpresa() {
        when(eventoService.criarEvento(any(Eventos.class)))
                .thenThrow(new RecursoNaoEncontradoException("Empresa não informada ou inválida"));

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.criarEvento(evento));
    }

    @Test
    @DisplayName("GET /eventos/data/{data} deve retornar 200 com eventos da data")
    void buscarPelaDataDeveRetornar200() {
        LocalDate data = LocalDate.of(2025, 9, 20);
        when(eventoService.buscarEventoPelaData(data)).thenReturn(List.of(evento));

        ResponseEntity<List<Eventos>> response = controller.buscarPelaData(data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /eventos/data/{data} deve propagar exceção quando não há eventos na data")
    void buscarPelaDataDevePropagarExcecao() {
        LocalDate data = LocalDate.of(2000, 1, 1);
        when(eventoService.buscarEventoPelaData(data))
                .thenThrow(new RecursoNaoEncontradoException("Nenhum evento nessa data"));

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.buscarPelaData(data));
    }

    @Test
    @DisplayName("GET /eventos/status/{status} deve retornar 200 com eventos filtrados")
    void buscarPeloStatusDeveRetornar200() {
        when(eventoService.buscarEventoPeloStatus("ATIVO")).thenReturn(List.of(evento));

        ResponseEntity<List<Eventos>> response = controller.buscarPeloStatus("ATIVO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /eventos/status/{status} deve propagar exceção com status inválido")
    void buscarPeloStatusDevePropagarExcecaoStatusInvalido() {
        when(eventoService.buscarEventoPeloStatus("INVALIDO"))
                .thenThrow(new IllegalArgumentException("Status inválido: INVALIDO"));

        assertThrows(IllegalArgumentException.class, () -> controller.buscarPeloStatus("INVALIDO"));
    }

    @Test
    @DisplayName("PUT /eventos/{id} deve retornar 200 com evento atualizado")
    void atualizarEventoPorIdDeveRetornar200() {
        when(eventoService.atualizarPorId(eq(1L), any(Eventos.class))).thenReturn(evento);

        ResponseEntity<Eventos> response = controller.atualizarEventoPorId(1L, evento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /eventos/{id} deve propagar exceção quando evento não existe")
    void atualizarEventoPorIdDevePropagarExcecao() {
        when(eventoService.atualizarPorId(eq(99L), any(Eventos.class)))
                .thenThrow(new RecursoNaoEncontradoException("Nenhum evento encontrado com esse id"));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.atualizarEventoPorId(99L, new Eventos()));
    }

    @Test
    @DisplayName("DELETE /eventos/{id} deve retornar 204")
    void deletarPorIdDeveRetornar204() {
        doNothing().when(eventoService).deletarPorId(1L);

        ResponseEntity<Void> response = controller.deletarPorId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventoService, times(1)).deletarPorId(1L);
    }

    @Test
    @DisplayName("DELETE /eventos/{id} deve propagar exceção quando evento não existe")
    void deletarPorIdDevePropagarExcecao() {
        doThrow(new RecursoNaoEncontradoException("Nenhum evento encontrado com esse id"))
                .when(eventoService).deletarPorId(99L);

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.deletarPorId(99L));
    }
}
