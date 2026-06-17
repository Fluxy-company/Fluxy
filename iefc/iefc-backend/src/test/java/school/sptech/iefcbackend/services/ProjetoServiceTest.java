package school.sptech.iefcbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.iefcbackend.exception.RecursoNaoEncontradoException;
import school.sptech.iefcbackend.models.Projeto;
import school.sptech.iefcbackend.repository.ProjetoRepository;

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
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private ProjetoService service;

    private Projeto projeto;

    @BeforeEach
    void setUp() {
        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto de Ciencia");
        projeto.setDescricao("Sistema completo de vendas");
        projeto.setDataInicio(LocalDate.of(2024, 1, 10));
        projeto.setDataFim(LocalDate.of(2024, 12, 31));
        projeto.setStatus(Projeto.StatusProjeto.EM_ANDAMENTO);
    }

    @Test
    @DisplayName("buscarTodos deve retornar todos os projetos")
    void buscarTodosDeveRetornarTodos() {
        when(projetoRepository.findAll()).thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Projeto de Ciencia", resultado.get(0).getNome());
        verify(projetoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há projetos")
    void buscarTodosDeveRetornarListaVazia() {
        when(projetoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Projeto> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(projetoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarProjetoPorNome deve retornar projeto quando nome existe")
    void buscarProjetoPorNomeDeveRetornar() {
        when(projetoRepository.findByNome("Projeto de Ciencia")).thenReturn(Optional.of(projeto));

        Projeto resultado = service.buscarProjetoPorNome("Projeto de Ciencia");

        assertNotNull(resultado);
        assertEquals("Projeto de Ciencia", resultado.getNome());
        verify(projetoRepository, times(1)).findByNome("Projeto de Ciencia");
    }

    @Test
    @DisplayName("buscarProjetoPorNome deve lançar exceção quando nome não existe")
    void buscarProjetoPorNomeDeveLancarExcecao() {
        when(projetoRepository.findByNome("Inexistente")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorNome("Inexistente"));
        verify(projetoRepository, times(1)).findByNome("Inexistente");
    }

    @Test
    @DisplayName("buscarProjetoPorDataInicio deve retornar lista de projetos")
    void buscarProjetoPorDataInicioDeveRetornar() {
        LocalDate data = LocalDate.of(2024, 1, 10);
        when(projetoRepository.findAllByDataInicio(data)).thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarProjetoPorDataInicio(data);

        assertEquals(1, resultado.size());
        verify(projetoRepository, times(1)).findAllByDataInicio(data);
    }

    @Test
    @DisplayName("buscarProjetoPorDataInicio deve lançar exceção quando não há projetos na data")
    void buscarProjetoPorDataInicioDeveLancarExcecao() {
        LocalDate data = LocalDate.of(2000, 1, 1);
        when(projetoRepository.findAllByDataInicio(data)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorDataInicio(data));
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve retornar projetos com o status informado")
    void buscarProjetoPorStatusDeveRetornar() {
        when(projetoRepository.findAllByStatus(Projeto.StatusProjeto.EM_ANDAMENTO))
                .thenReturn(List.of(projeto));

        List<Projeto> resultado = service.buscarProjetoPorStatus("EM_ANDAMENTO");

        assertEquals(1, resultado.size());
        assertEquals(Projeto.StatusProjeto.EM_ANDAMENTO, resultado.get(0).getStatus());
        verify(projetoRepository, times(1)).findAllByStatus(Projeto.StatusProjeto.EM_ANDAMENTO);
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve lançar exceção para status inválido")
    void buscarProjetoPorStatusDeveLancarExcecaoStatusInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarProjetoPorStatus("STATUS_INVALIDO"));
    }

    @Test
    @DisplayName("buscarProjetoPorStatus deve lançar exceção quando não há projetos com o status")
    void buscarProjetoPorStatusDeveLancarExcecaoSemProjetos() {
        when(projetoRepository.findAllByStatus(Projeto.StatusProjeto.CANCELADO))
                .thenReturn(Collections.emptyList());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarProjetoPorStatus("CANCELADO"));
    }

    @Test
    @DisplayName("atualizarPorId deve atualizar os dados do projeto")
    void atualizarPorIdDeveAtualizar() {
        Projeto projetoAtualizado = new Projeto();
        projetoAtualizado.setNome("Projeto de Ciencia v2");
        projetoAtualizado.setDescricao("Nova versão");
        projetoAtualizado.setDataInicio(LocalDate.of(2024, 2, 1));
        projetoAtualizado.setDataFim(LocalDate.of(2024, 12, 31));
        projetoAtualizado.setStatus(Projeto.StatusProjeto.CONCLUIDO);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        Projeto resultado = service.atualizarPorId(1L, projetoAtualizado);

        assertNotNull(resultado);
        verify(projetoRepository, times(1)).findById(1L);
        verify(projetoRepository, times(1)).save(any(Projeto.class));
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando projeto não existe")
    void atualizarPorIdDeveLancarExcecao() {
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        Projeto projetoAtualizado = new Projeto();
        projetoAtualizado.setNome("Não existe");

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(99L, projetoAtualizado));
    }

    @Test
    @DisplayName("deletarPorId deve remover o projeto existente")
    void deletarPorIdDeveRemover() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        doNothing().when(projetoRepository).delete(projeto);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(projetoRepository, times(1)).delete(projeto);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando projeto não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
    }
}
