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
import school.sptech.iefcbackend.services.EmpresaService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaControllerTest {

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private EmpresaController controller;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("TechCorp");
        empresa.setCnpj("12.345.678/0001-99");
        empresa.setTelefone("11999999999");
    }

    @Test
    @DisplayName("POST /empresas deve retornar 201 ao salvar empresa")
    void salvarEmpresaDeveRetornar201() {
        doNothing().when(empresaService).salvarEmpresa(any(Empresa.class));

        ResponseEntity<Void> response = controller.salvarEmpresa(empresa);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(empresaService, times(1)).salvarEmpresa(any(Empresa.class));
    }

    @Test
    @DisplayName("GET /empresas deve retornar 200 com lista de empresas")
    void buscarTodosDeveRetornar200ComEmpresas() {
        when(empresaService.buscarTodos()).thenReturn(List.of(empresa));

        ResponseEntity<List<Empresa>> response = controller.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("TechCorp", response.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("GET /empresas deve retornar 200 com lista vazia")
    void buscarTodosDeveRetornar200ComListaVazia() {
        when(empresaService.buscarTodos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Empresa>> response = controller.buscarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /empresas/{cnpj} deve retornar 200 com empresa encontrada")
    void acharPeloIdDeveRetornar200() {
        when(empresaService.buscarEmpresaPorCnpj("12.345.678/0001-99")).thenReturn(empresa);

        ResponseEntity<Empresa> response = controller.acharPeloId("12.345.678/0001-99");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("TechCorp", response.getBody().getNome());
    }

    @Test
    @DisplayName("GET /empresas/{cnpj} deve propagar exceção quando não encontrada")
    void acharPeloIdDevePropagarExcecao() {
        when(empresaService.buscarEmpresaPorCnpj("00.000.000/0000-00"))
                .thenThrow(new RecursoNaoEncontradoException("Cnpj não encontrado!"));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.acharPeloId("00.000.000/0000-00"));
    }

    @Test
    @DisplayName("PUT /empresas/{id} deve retornar 200 ao atualizar empresa")
    void atualizarPorIdDeveRetornar200() {
        doNothing().when(empresaService).atualizarPorId(eq(1L), any(Empresa.class));

        ResponseEntity<Empresa> response = controller.atualizarPorId(1L, empresa);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(empresaService, times(1)).atualizarPorId(eq(1L), any(Empresa.class));
    }

    @Test
    @DisplayName("PUT /empresas/{id} deve propagar exceção quando empresa não existe")
    void atualizarPorIdDevePropagarExcecao() {
        doThrow(new RecursoNaoEncontradoException("Empresa não encontrada"))
                .when(empresaService).atualizarPorId(eq(99L), any(Empresa.class));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.atualizarPorId(99L, empresa));
    }

    @Test
    @DisplayName("DELETE /empresas/{id} deve retornar 204 ao deletar empresa")
    void deletarPorIdDeveRetornar204() {
        doNothing().when(empresaService).deletarPorId(1L);

        ResponseEntity<Void> response = controller.deletarPorId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(empresaService, times(1)).deletarPorId(1L);
    }

    @Test
    @DisplayName("DELETE /empresas/{id} deve propagar exceção quando empresa não existe")
    void deletarPorIdDevePropagarExcecao() {
        doThrow(new RecursoNaoEncontradoException("Sem registros nesse id"))
                .when(empresaService).deletarPorId(99L);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> controller.deletarPorId(99L));
    }
}