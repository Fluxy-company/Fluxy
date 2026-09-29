package school.sptech.iefcbackend.usecase.projeto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.domain.entity.Empresa;
import school.sptech.iefcbackend.domain.entity.Projeto;
import school.sptech.iefcbackend.domain.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.domain.port.ProjetoRepositoryPort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @Mock
    private ProjetoRepositoryPort repository;

    @InjectMocks
    private ProjetoService service;

    private Projeto projeto;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Parceira");

        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Inclusão Digital");
        projeto.setDescricao("Capacitação em tecnologia");
        projeto.setDataInicio(LocalDate.of(2025, 1, 10));
        projeto.setDataFim(LocalDate.of(2025, 12, 20));
        projeto.setStatus(Projeto.StatusProjeto.EM_ANDAMENTO);
        projeto.setEmpresa(empresa);
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista com projetos")
    void buscarTodosDeveRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Projeto Inclusão Digital", resultado.get(0).getNome());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há projetos")
    void buscarTodosDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Projeto> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarProjetoPorNome deve retornar projeto quando nome existe")
    void buscarProjetoPorNomeDeveRetornarProjeto() {
        when(repository.findByNome("Projeto Inclusão Digital")).thenReturn(Optional.of(projeto));

        Projeto resultado = service.buscarProjetoPorNome("Projeto Inclusão Digital");

        assertNotNull(resultado);
        assertEquals("Projeto Inclusão Digital", resultado.getNome());
        verify(repository, times(1)).findByNome("Projeto Inclusão Digital");
    }

    @Test
    @DisplayName("buscarProjetoPorNome deve lançar exceção quando projeto não encontrado")
    void buscarProjetoPorNomeDeveLancarExcecao() {
        when(repository.findByNome("Inexistente")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorNome("Inexistente"));
        verify(repository, times(1)).findByNome("Inexistente");
    }

    @Test
    @DisplayName("buscarProjetoPorDataInicio deve retornar projetos da data informada")
    void buscarProjetoPorDataInicioDeveRetornarLista() {
        LocalDate data = LocalDate.of(2025, 1, 10);
        when(repository.findAllByDataInicio(data)).thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarProjetoPorDataInicio(data);

        assertEquals(1, resultado.size());
        assertEquals(data, resultado.get(0).getDataInicio());
        verify(repository, times(1)).findAllByDataInicio(data);
    }

    @Test
    @DisplayName("buscarProjetoPorDataInicio deve lançar exceção quando nenhum projeto encontrado")
    void buscarProjetoPorDataInicioDeveLancarExcecao() {
        LocalDate data = LocalDate.of(2099, 1, 1);
        when(repository.findAllByDataInicio(data)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorDataInicio(data));
        verify(repository, times(1)).findAllByDataInicio(data);
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve retornar projetos com o status correspondente")
    void buscarProjetoPorStatusDeveRetornarLista() {
        when(repository.findAllByStatus(Projeto.StatusProjeto.EM_ANDAMENTO))
                .thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarProjetoPorStatus("EM_ANDAMENTO");

        assertEquals(1, resultado.size());
        assertEquals(Projeto.StatusProjeto.EM_ANDAMENTO, resultado.get(0).getStatus());
        verify(repository, times(1)).findAllByStatus(Projeto.StatusProjeto.EM_ANDAMENTO);
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve lançar exceção quando status é inválido")
    void buscarProjetoPorStatusDeveLancarExcecaoStatusInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarProjetoPorStatus("STATUS_INVALIDO"));
        verify(repository, never()).findAllByStatus(any());
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve lançar exceção quando nenhum projeto possui o status")
    void buscarProjetoPorStatusDeveLancarExcecaoNenhumProjeto() {
        when(repository.findAllByStatus(Projeto.StatusProjeto.CONCLUIDO))
                .thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorStatus("CONCLUIDO"));
        verify(repository, times(1)).findAllByStatus(Projeto.StatusProjeto.CONCLUIDO);
    }

    @Test
    @DisplayName("salvarProjeto deve salvar quando empresa é informada")
    void salvarProjetoDeveSalvar() {
        when(repository.save(projeto)).thenReturn(projeto);

        Projeto resultado = service.salvarProjeto(projeto);

        assertNotNull(resultado);
        assertEquals("Projeto Inclusão Digital", resultado.getNome());
        verify(repository, times(1)).save(projeto);
    }

    @Test
    @DisplayName("salvarProjeto deve lançar exceção quando empresa é nula")
    void salvarProjetoDeveLancarExcecaoEmpresaNula() {
        projeto.setEmpresa(null);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.salvarProjeto(projeto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("salvarProjeto deve lançar exceção quando id da empresa é nulo")
    void salvarProjetoDeveLancarExcecaoIdEmpresaNulo() {
        empresa.setId(null);
        projeto.setEmpresa(empresa);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.salvarProjeto(projeto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("atualizarPorId deve atualizar os campos do projeto")
    void atualizarPorIdDeveAtualizar() {
        Projeto projetoAtualizado = new Projeto();
        projetoAtualizado.setNome("Projeto Atualizado");
        projetoAtualizado.setDescricao("Nova descrição");
        projetoAtualizado.setDataInicio(LocalDate.of(2025, 2, 1));
        projetoAtualizado.setDataFim(LocalDate.of(2025, 11, 30));
        projetoAtualizado.setStatus(Projeto.StatusProjeto.CONCLUIDO);

        when(repository.findById(1L)).thenReturn(Optional.of(projeto));
        when(repository.save(any(Projeto.class))).thenReturn(projeto);

        Projeto resultado = service.atualizarPorId(1L, projetoAtualizado);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(projeto);
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando projeto não existe")
    void atualizarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(99L, projeto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deletarPorId deve remover o projeto existente")
    void deletarPorIdDeveRemover() {
        when(repository.findById(1L)).thenReturn(Optional.of(projeto));
        doNothing().when(repository).delete(projeto);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(repository, times(1)).delete(projeto);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando projeto não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
        verify(repository, never()).delete(any());
    }
}
