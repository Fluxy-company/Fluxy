package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.iefcbackend.dto.UsuarioRequestDTO;
import school.sptech.iefcbackend.dto.UsuarioResponseDTO;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.UsuarioRepository;
import school.sptech.iefcbackend.services.UsuarioService;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Controller para salvar e editar usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService service, UsuarioRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.service = service;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
@Operation(summary = "Busca todos os usuarios", description = "Método que busca todos os usuarios")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum cadastro encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping(value = "{id}")
    @Operation(summary = "Busca usuario por Id", description = "Método que busca o usuario pelo Id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> acharPeloId(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.acharPeloId(id));
    }

    @GetMapping("/email")
    @Operation(summary = "Busca usuario por email", description = "Método que busca o usuario pelo email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse email")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@Valid @RequestParam String email){
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @PostMapping
    @Operation(summary = "Salva os dados do usuario", description = "Método que salva os dados do usuario")
    @ApiResponse(responseCode = "201",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)),
            description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@Valid @RequestBody UsuarioRequestDTO dto){

        Role basicRole = Role.BASIC;

        var userDaTabela = repository.findByEmail(dto.getEmail());

        if(userDaTabela.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        var usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setSobrenome(dto.getSobrenome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRoles(Set.of(basicRole));
       repository.save(usuario);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Edita os dados do usuario por Id", description = "Método que edita os dados do usuario pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuarioPorId(@Valid @PathVariable Long id, @RequestBody UsuarioRequestDTO dto){
        return ResponseEntity.ok (service.atualizarUsuarioPorId(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta usuario por Id", description = "Método que deleta o usuario pelo Id")
    @ApiResponse(responseCode = "204", description = "Usuario deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id){
        service.deletarPorId(id);

        return ResponseEntity.noContent().build();
    }

}
