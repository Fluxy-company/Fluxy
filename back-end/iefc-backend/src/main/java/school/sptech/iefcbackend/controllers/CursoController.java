package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.services.CursoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@Tag(name = "Curso", description = "Controller para salvar e editar dados das cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Salva os dados da cursos", description = "Método que salva os dados da cursos")
    @ApiResponse(responseCode = "201",
            content = @Content(schema = @Schema(implementation = Curso.class)),
            description = "Cursos criada com sucesso")
    @ApiResponse(responseCode = "409", description = "Curso ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> postar(@RequestBody Curso curso){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.postar(curso));
    }

    @GetMapping
    @Operation(summary = "Busca todas as curso", description = "Método que busca todas as curso")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum curso encontrada")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.Listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por id", description = "Método que busca a empresa pelo id")
    @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta a curso pelo Id", description = "Método que deleta a curso pelo Id")
    @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<?> deletar(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita os dados da curso pelo Id", description = "Método que edita os dados da curso pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso){
        return ResponseEntity.ok(service.atualizar(id, curso));
    }
}