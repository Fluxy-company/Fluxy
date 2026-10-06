package school.sptech.iefcbackend.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.domain.entity.Curso;
import school.sptech.iefcbackend.domain.entity.Video;
import school.sptech.iefcbackend.usecase.curso.CursoService;
import school.sptech.iefcbackend.usecase.video.VideoService;

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
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Salva os dados da cursos", description = "Método que salva os dados da cursos")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Curso.class)), description = "Cursos criada com sucesso")
    @ApiResponse(responseCode = "409", description = "Curso ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> postar(@RequestBody Curso curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.postar(curso));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Busca cursos com paginação", description = "Método que lista cursos de forma paginada com suporte a ordenação")
    @ApiResponse(responseCode = "200", description = "Página de cursos retornada com sucesso")
    public ResponseEntity<Page<Curso>> listar(
            @PageableDefault(size = 10, sort = "titulo", direction = Sort.Direction.ASC)
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por id", description = "Método que busca o curso pelo id")
    @ApiResponse(responseCode = "200", description = "Curso encontrado com sucesso")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/tema/{temaId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Buscar cursos por tema", description = "Método que busca cursos pelo tema")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum curso encontrado nesse tema")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Curso>> buscarPorTema(@PathVariable Long temaId) {
        return ResponseEntity.ok(service.listarPorTema(temaId));
    }

    @GetMapping("/{id}/videos")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Buscar videos de um curso", description = "Método que busca todos os videos de um curso")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum video encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Video>> buscarVideosPorCurso(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.buscarPorCursoId(id));
    }

    @PostMapping("/{id}/videos")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Adiciona um video a um curso", description = "Método que cria um video vinculado ao curso")
    @ApiResponse(responseCode = "201", description = "Video criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Video> adicionarVideo(@PathVariable Long id, @RequestBody Video video) {
        Curso curso = service.buscarPorId(id);
        video.setCurso(curso);
        videoService.salvarVideo(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Deleta a curso pelo Id", description = "Método que deleta a curso pelo Id")
    @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Edita os dados da curso pelo Id", description = "Método que edita os dados da curso pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso) {
        return ResponseEntity.ok(service.atualizar(id, curso));
    }
}