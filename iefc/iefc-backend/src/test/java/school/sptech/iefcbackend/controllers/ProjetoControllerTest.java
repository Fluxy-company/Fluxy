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
import school.sptech.iefcbackend.models.Projeto;
import school.sptech.iefcbackend.services.ProjetoService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoControllerTest {

    @Mock
    private ProjetoService projetoService;

    @InjectMocks
    private ProjetoController controller;

    private Projeto projeto;

    @BeforeEach
    void setUp() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);

        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Sistema IEFC");
        projeto.setDescricao("Plataforma educacional");
        projeto.setDataInicio(LocalDate.of(2025, 1, 1));
        projeto.setDataFim(LocalDate.of(2025, 12, 31));
        projeto.setStatus(Projeto.StatusProjeto.EM_ANDAMENTO);
        projeto.setEmpresa(empresa);
    }

    @Test
    @DisplayName("POST /projetos deve retornar 201 com projeto criado")
    void salvarProjetoDeveRetornar201() {
        when(projetoService.salvarProjeto(any(Projeto.class))).thenReturn(projeto);

        ResponseEntity<Projeto> response = controller.salvarProjeto(projeto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sistema IEFC", response.getBody().getNome());
    }

    @Test
    @DisplayName("POST /projetos deve propagar exceção quando empresa não informada")
    void salvarProjetoDevePropagarExcecaoSemEmpresa() {
        when(projetoService.salvarProjeto(any(Projeto.class)))
                .thenThrow(new RecursoNaoEncontradoException("Empresa não informada ou inválida"));

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.salvarProjeto(projeto));
    }

    @Test
    @DisplayName("GET /projetos deve retornar 200 com lista de projetos")
    void buscarTodosDeveRetornar200ComProjetos() {
        when(projetoService.buscarTodos()).thenReturn(List.of(projeto));

        ResponseEntity<List<Projeto>> response = controller.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /projetos deve retornar 200 com lista vazia")
    void buscarTodosDeveRetornar200ComListaVazia() {
        when(projetoService.buscarTodos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Projeto>> response = controller.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /projetos/nome/{nome} deve retornar 200 com projeto encontrado")
    void pesquisarProjetoPeloNomeDeveRetornar200() {
        when(projetoService.buscarProjetoPorNome("Sistema IEFC")).thenReturn(projeto);

        ResponseEntity<Projeto> response = controller.pesquisarProjetoPeloNome("Sistema IEFC");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sistema IEFC", response.getBody().getNome());
    }

    @Test
    @DisplayName("GET /projetos/nome/{nome} deve propagar exceção quando não encontrado")
    void pesquisarProjetoPeloNomeDevePropagarExcecao() {
        when(projetoService.buscarProjetoPorNome("Inexistente"))
                .thenThrow(new RecursoNaoEncontradoException("Não há nenhum projeto com este nome."));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.pesquisarProjetoPeloNome("Inexistente"));
    }

    @Test
    @DisplayName("GET /projetos/dataInicio/{data} deve retornar 200 com projetos encontrados")
    void pesquisarProjetoPelaDataInicioDeveRetornar200() {
        LocalDate data = LocalDate.of(2025, 1, 1);
        when(projetoService.buscarProjetoPorDataInicio(data)).thenReturn(List.of(projeto));

        ResponseEntity<List<Projeto>> response = controller.pesquisarProjetoPelaDataInicio(data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /projetos/status/{status} deve retornar 200 com projetos filtrados")
    void pesquisarProjetoPeloStatusDeveRetornar200() {
        when(projetoService.buscarProjetoPorStatus("EM_ANDAMENTO")).thenReturn(List.of(projeto));

        ResponseEntity<List<Projeto>> response = controller.pesquisarProjetoPeloStatus("EM_ANDAMENTO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /projetos/status/{status} deve propagar exceção com status inválido")
    void pesquisarProjetoPeloStatusDevePropagarExcecaoStatusInvalido() {
        when(projetoService.buscarProjetoPorStatus("INVALIDO"))
                .thenThrow(new IllegalArgumentException("Status inválido: INVALIDO"));

        assertThrows(IllegalArgumentException.class,
                () -> controller.pesquisarProjetoPeloStatus("INVALIDO"));
    }

    @Test
    @DisplayName("PUT /projetos/{id} deve retornar 200 com projeto atualizado")
    void atualizarProjetoPorIdDeveRetornar200() {
        when(projetoService.atualizarPorId(eq(1L), any(Projeto.class))).thenReturn(projeto);

        ResponseEntity<Projeto> response = controller.atualizarProjetoPorId(1L, projeto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /projetos/{id} deve propagar exceção quando projeto não existe")
    void atualizarProjetoPorIdDevePropagarExcecao() {
        when(projetoService.atualizarPorId(eq(99L), any(Projeto.class)))
                .thenThrow(new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação."));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.atualizarProjetoPorId(99L, new Projeto()));
    }

    @Test
    @DisplayName("DELETE /projetos/{id} deve retornar 204")
    void deletarPorIdDeveRetornar204() {
        doNothing().when(projetoService).deletarPorId(1L);

        ResponseEntity<Void> response = controller.deletarPorId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(projetoService, times(1)).deletarPorId(1L);
    }

    @Test
    @DisplayName("DELETE /projetos/{id} deve propagar exceção quando projeto não existe")
    void deletarPorIdDevePropagarExcecao() {
        doThrow(new RecursoNaoEncontradoException("Nenhum projeto foi encontrado com essa identificação."))
                .when(projetoService).deletarPorId(99L);

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.deletarPorId(99L));
    }
}
