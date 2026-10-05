package school.sptech.iefcbackend.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.usecase.usuario.UsuarioService;
import school.sptech.iefcbackend.web.dto.UsuarioAdminRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioColaboradorRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.web.dto.UsuarioResponseDTO;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Controller para salvar e editar usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Busca todos os usuarios de forma paginada", description = "Método que busca todos os usuarios com suporte a paginação e ordenação")
    @ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarTodos(
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "nome", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        return ResponseEntity.ok(service.buscarTodos(pageable));
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Busca usuario por Id", description = "Método que busca o usuario pelo Id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> acharPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.acharPeloId(id));
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Busca usuario por email", description = "Método que busca o usuario pelo email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse email")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@Valid @RequestParam String email) {
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @PostMapping
    @Operation(summary = "Salva os dados do usuario", description = "Método que salva os dados do usuario (Autocadastro - Aluno)", security = {})
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)), description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO response = service.salvarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Cria usuario com role específico (Admin)", description = "Método que permite admin criar usuário com qualquer role (ADMIN, COLABORADOR, ALUNO)")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)), description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvarUsuarioComoAdmin(@Valid @RequestBody UsuarioAdminRequestDTO dto) {
        UsuarioResponseDTO response = service.salvarUsuarioComoAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/colaboradores")
    @Operation(summary = "Autocadastro de colaborador", description = "Colaborador se cadastra e fica com status PENDENTE até o admin aprovar; só pode logar depois de aprovado", security = {})
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)), description = "Cadastro criado com sucesso, aguardando aprovação")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> cadastrarColaborador(@Valid @RequestBody UsuarioColaboradorRequestDTO dto) {
        UsuarioResponseDTO response = service.cadastrarColaborador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pendentes")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Lista cadastros pendentes de aprovação", description = "Método que lista todos os colaboradores com status PENDENTE (apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<UsuarioResponseDTO>> listarPendentes() {
        return ResponseEntity.ok(service.listarPendentes());
    }

    @GetMapping("/pendentes/paginado")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Lista cadastros pendentes de forma paginada", description = "Método paginado que lista colaboradores com status PENDENTE (apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarPendentesPaginado(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPendentesPaginado(pageable));
    }

    @PutMapping("/{id}/aprovar")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Aprova cadastro de colaborador", description = "Método que aprova um cadastro PENDENTE, liberando o login (apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Cadastro aprovado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> aprovarCadastro(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovarCadastro(id));
    }

    @PutMapping("/{id}/reprovar")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Reprova cadastro de colaborador", description = "Método que reprova um cadastro PENDENTE, bloqueando o login (apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Cadastro reprovado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> reprovarCadastro(@PathVariable Long id) {
        return ResponseEntity.ok(service.reprovarCadastro(id));
    }

    @GetMapping("/admin-users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Lista todos os usuarios administradores", description = "Método que busca todos os usuários com role ADMIN (apenas ADMIN pode acessar)")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodosAdmins() {
        return ResponseEntity.ok(service.buscarTodosAdmins());
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Edita os dados do usuario por Id", description = "Método que edita os dados do usuario pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuarioPorId(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarUsuarioPorId(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Deleta usuario por Id", description = "Método que deleta o usuario pelo Id")
    @ApiResponse(responseCode = "204", description = "Usuario deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id) {
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
