package school.sptech.fluxybackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import school.sptech.fluxybackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.controllers.dto.mapper.UsuarioDTOMapper;
import school.sptech.fluxybackend.exception.EmailJaCadastradoException;
import school.sptech.fluxybackend.exception.RecursoNaoEncontradoException;
import school.sptech.fluxybackend.models.Usuario;
import school.sptech.fluxybackend.repository.UsuarioRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioDTOMapper mapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNome("Maria");
        usuario.setSobrenome("Silva");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("senha123");

        requestDTO = UsuarioRequestDTO.builder()
                .nome("Maria")
                .sobrenome("Silva")
                .email("maria@email.com")
                .senha("senha123")
                .build();

        responseDTO = UsuarioResponseDTO.builder()
                .idUsuario(1L)
                .nome("Maria")
                .sobrenome("Silva")
                .email("maria@email.com")
                .build();
    }

    // --- buscarTodos ---

    @Test
    @DisplayName("buscarTodos deve retornar lista com usuários")
    void buscarTodosDeveRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        when(mapper.toDTO(usuario)).thenReturn(responseDTO);

        List<UsuarioResponseDTO> resultado = service.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).getNome());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarTodos deve retornar lista vazia quando não há usuários")
    void buscarTodosDeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioResponseDTO> resultado = service.buscarTodos();

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    // --- acharPeloId ---

    @Test
    @DisplayName("acharPeloId deve retornar usuário quando ID existe")
    void acharPeloIdDeveRetornarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(mapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = service.acharPeloId(1L);

        assertNotNull(resultado);
        assertEquals("Maria", resultado.getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("acharPeloId deve lançar exceção quando ID não existe")
    void acharPeloIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.acharPeloId(99L));
        verify(repository, times(1)).findById(99L);
    }

    // --- buscarUsuarioPorEmail ---

    @Test
    @DisplayName("buscarUsuarioPorEmail deve retornar usuário quando email existe")
    void buscarUsuarioPorEmailDeveRetornar() {
        when(repository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));
        when(mapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = service.buscarUsuarioPorEmail("maria@email.com");

        assertNotNull(resultado);
        assertEquals("maria@email.com", resultado.getEmail());
        verify(repository, times(1)).findByEmail("maria@email.com");
    }

    @Test
    @DisplayName("buscarUsuarioPorEmail deve lançar exceção quando email não existe")
    void buscarUsuarioPorEmailDeveLancarExcecao() {
        when(repository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarUsuarioPorEmail("naoexiste@email.com"));
    }

    // --- salvarUsuario ---

    @Test
    @DisplayName("salvarUsuario deve salvar e retornar o usuário")
    void salvarUsuarioDeveSalvar() {
        when(repository.existsByEmail("maria@email.com")).thenReturn(false);
        when(mapper.toEntity(requestDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("senha123")).thenReturn("hash_senha");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = service.salvarUsuario(requestDTO);

        assertNotNull(resultado);
        assertEquals("Maria", resultado.getNome());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("salvarUsuario deve lançar exceção quando email já está cadastrado")
    void salvarUsuarioDeveLancarExcecaoEmailDuplicado() {
        when(repository.existsByEmail("maria@email.com")).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> service.salvarUsuario(requestDTO));
        verify(repository, never()).save(any(Usuario.class));
    }

    // --- atualizarUsuarioPorId ---

    @Test
    @DisplayName("atualizarUsuarioPorId deve atualizar os dados do usuário")
    void atualizarUsuarioPorIdDeveAtualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("maria@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("hash_senha");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = service.atualizarUsuarioPorId(1L, requestDTO);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("atualizarUsuarioPorId deve lançar exceção quando usuário não existe")
    void atualizarUsuarioPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarUsuarioPorId(99L, requestDTO));
    }

    @Test
    @DisplayName("atualizarUsuarioPorId deve lançar exceção quando novo email já está em uso")
    void atualizarUsuarioPorIdDeveLancarExcecaoEmailDuplicado() {
        usuario.setEmail("outro@email.com"); // email atual diferente do novo
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("maria@email.com")).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> service.atualizarUsuarioPorId(1L, requestDTO));
        verify(repository, never()).save(any(Usuario.class));
    }

    // --- deletarPorId ---

    @Test
    @DisplayName("deletarPorId deve remover o usuário existente")
    void deletarPorIdDeveRemover() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(repository).delete(usuario);

        assertDoesNotThrow(() -> service.deletarPorId(1L));
        verify(repository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("deletarPorId deve lançar exceção quando usuário não existe")
    void deletarPorIdDeveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.deletarPorId(99L));
    }
}