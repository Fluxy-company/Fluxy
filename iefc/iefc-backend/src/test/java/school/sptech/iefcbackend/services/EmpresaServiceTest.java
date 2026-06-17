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
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.EmpresaRepository;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EmpresaService service;

    private Empresa empresa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Tech Ltda");
        empresa.setCnpj("12.345.678/0001-99");
        empresa.setTelefone("11999999999");
        empresa.setUsuario(usuario);
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista com empresas")
    void buscarTodosDeveRetornarLista() {
        when(empresaRepository.findAll()).thenReturn(List.of(empresa));

        List<Empresa> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Tech Ltda", resultado.get(0).getNome());
        verify(empresaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há empresas")
    void buscarTodosDeveRetornarListaVazia() {
        when(empresaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Empresa> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(empresaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarEmpresaPorCnpj deve retornar empresa quando CNPJ existe")
    void buscarEmpresaPorCnpjDeveRetornarEmpresa() {
        when(empresaRepository.findByCnpj("12.345.678/0001-99")).thenReturn(Optional.of(empresa));

        Empresa resultado = service.buscarEmpresaPorCnpj("12.345.678/0001-99");

        assertNotNull(resultado);
        assertEquals("Tech Ltda", resultado.getNome());
        verify(empresaRepository, times(1)).findByCnpj("12.345.678/0001-99");
    }

    @Test
    @DisplayName("buscarEmpresaPorCnpj deve lançar exceção quando CNPJ não existe")
    void buscarEmpresaPorCnpjDeveLancarExcecao() {
        when(empresaRepository.findByCnpj("00.000.000/0000-00")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarEmpresaPorCnpj("00.000.000/0000-00"));
        verify(empresaRepository, times(1)).findByCnpj("00.000.000/0000-00");
    }

    @Test
    @DisplayName("salvarEmpresa deve salvar com usuário válido")
    void salvarEmpresaDeveSalvarComUsuarioValido() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertDoesNotThrow(() -> service.salvarEmpresa(empresa));

        verify(usuarioRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("salvarEmpresa deve salvar sem usuario quando usuario é nulo")
    void salvarEmpresaDeveSalvarSemUsuario() {
        empresa.setUsuario(null);

        assertDoesNotThrow(() -> service.salvarEmpresa(empresa));

        verify(usuarioRepository, never()).findById(any());
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("salvarEmpresa deve salvar sem usuario quando id do usuario é nulo")
    void salvarEmpresaDeveSalvarQuandoIdUsuarioNulo() {
        Usuario usuarioSemId = new Usuario();
        // id permanece null
        empresa.setUsuario(usuarioSemId);

        assertDoesNotThrow(() -> service.salvarEmpresa(empresa));

        verify(usuarioRepository, never()).findById(any());
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("salvarEmpresa deve lançar exceção quando usuario vinculado não existe no banco")
    void salvarEmpresaDeveLancarExcecaoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.salvarEmpresa(empresa));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    @DisplayName("salvarEmpresa deve lançar exceção quando id do usuario é inválido (zero ou negativo)")
    void salvarEmpresaDeveLancarExcecaoIdUsuarioInvalido() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setId(0L);
        empresa.setUsuario(usuarioInvalido);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.salvarEmpresa(empresa));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    @DisplayName("atualizarPorId deve atualizar os dados da empresa sem usuario")
    void atualizarPorIdDeveAtualizarSemUsuario() {
        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setNome("Tech S/A");
        empresaAtualizada.setCnpj("99.999.999/0001-00");
        empresaAtualizada.setTelefone("11888888888");
        empresaAtualizada.setUsuario(null);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        assertDoesNotThrow(() -> service.atualizarPorId(1L, empresaAtualizada));

        verify(empresaRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("atualizarPorId deve atualizar os dados da empresa com usuario válido")
    void atualizarPorIdDeveAtualizarComUsuarioValido() {
        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setNome("Tech S/A");
        empresaAtualizada.setCnpj("99.999.999/0001-00");
        empresaAtualizada.setTelefone("11888888888");
        empresaAtualizada.setUsuario(usuario);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertDoesNotThrow(() -> service.atualizarPorId(1L, empresaAtualizada));

        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando empresa não existe")
    void atualizarPorIdDeveLancarExcecaoEmpresaNaoEncontrada() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setNome("Não existe");
        empresaAtualizada.setUsuario(null);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(99L, empresaAtualizada));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    @DisplayName("atualizarPorId deve lançar exceção quando usuario vinculado não existe")
    void atualizarPorIdDeveLancarExcecaoUsuarioNaoEncontrado() {
        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setNome("Tech S/A");
        empresaAtualizada.setCnpj("99.999.999/0001-00");
        empresaAtualizada.setTelefone("11888888888");
        empresaAtualizada.setUsuario(usuario);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarPorId(1L, empresaAtualizada));
        verify(empresaRepository, never()).save(any());
    }

    @Test
    @DisplayName("deletarPorId deve remover a empresa existente")
    void deletarPorIdDeveRemover() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        doNothing().when(empresaRepository).delete(empresa);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(empresaRepository, times(1)).delete(empresa);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando empresa não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
        verify(empresaRepository, never()).delete(any());
    }
}