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
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.services.CursoService;
import school.sptech.iefcbackend.services.VideoService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cursos")
@Tag(name = "Curso", description = "Controller para salvar e editar dados das cursos")
public class CursoController {

    private final CursoService service;
    private final VideoService videoService;

    public CursoController(CursoService service, VideoService videoService) {
        this.service = service;
        this.videoService = videoService;
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
    @Operation(summary = "Buscar curso por id", description = "Método que busca o curso pelo id")
    @ApiResponse(responseCode = "200", description = "Curso encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/tema/{temaId}")
    @Operation(summary = "Buscar cursos por tema", description = "Método que busca cursos pelo tema")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum curso encontrado nesse tema")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Curso>> buscarPorTema(@PathVariable Long temaId){
        return ResponseEntity.ok(service.listarPorTema(temaId));
    }

    @GetMapping("/{id}/videos")
    @Operation(summary = "Buscar videos de um curso", description = "Método que busca todos os videos de um curso")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum video encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Video>> buscarVideosPorCurso(@PathVariable Long id){
        return ResponseEntity.ok(videoService.buscarPorCursoId(id));
    }

    @PostMapping("/{id}/videos")
    @Operation(summary = "Adiciona um video a um curso", description = "Método que cria um video vinculado ao curso")
    @ApiResponse(responseCode = "201", description = "Video criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Video> adicionarVideo(@PathVariable Long id, @RequestBody Video video){
        Curso curso = service.buscarPorId(id);
        video.setCurso(curso);
        videoService.salvarVideo(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta a curso pelo Id", description = "Método que deleta a curso pelo Id")
    @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso")
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