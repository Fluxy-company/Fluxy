package school.sptech.fluxybackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.fluxybackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Controller para salvar e editar usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

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
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> acharPeloId(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.acharPeloId(id));
    }

    @GetMapping("/email")
    @Operation(summary = "Busca usuario por email", description = "Método que busca o usuario pelo email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse email")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@RequestParam String email){
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @PostMapping
    @Operation(summary = "Salva os dados do usuario", description = "Método que salva os dados do usuario")
    @ApiResponse(responseCode = "201",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)),
            description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody UsuarioRequestDTO dto){
        return ResponseEntity.status(201).body(service.salvarUsuario(dto));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Edita os dados do usuario por Id", description = "Método que edita os dados do usuario pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuarioPorId(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto){
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
