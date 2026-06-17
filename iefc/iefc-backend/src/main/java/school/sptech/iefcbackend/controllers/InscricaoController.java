package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.models.Curso;
import school.sptech.iefcbackend.models.Inscricao;
import school.sptech.iefcbackend.models.ProgressoAula;
import school.sptech.iefcbackend.services.InscricaoService;
import school.sptech.iefcbackend.services.ProgressoAulaService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/inscricoes")
@Tag(name = "Inscrição", description = "Controller para gerenciar inscrições e progresso dos usuários nos cursos")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final ProgressoAulaService progressoService;

    public InscricaoController(InscricaoService inscricaoService, ProgressoAulaService progressoService) {
        this.inscricaoService = inscricaoService;
        this.progressoService = progressoService;
    }

    // ---- Inscrições ----

    @PostMapping("/usuario/{usuarioId}/curso/{cursoId}")
    @Operation(summary = "Inscrever usuário em um curso", description = "Cria a inscrição do usuário no curso. Se já inscrito, retorna a inscrição existente.")
    @ApiResponse(responseCode = "201", description = "Inscrição criada com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário ou curso não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Inscricao> inscrever(@PathVariable Long usuarioId, @PathVariable Long cursoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscricaoService.inscrever(usuarioId, cursoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar cursos do usuário", description = "Retorna todas as inscrições de um usuário")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Inscricao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(inscricaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/curso/{cursoId}")
    @Operation(summary = "Verificar inscrição", description = "Verifica se o usuário está inscrito no curso")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<Map<String, Boolean>> verificarInscricao(@PathVariable Long usuarioId, @PathVariable Long cursoId) {
        boolean inscrito = inscricaoService.estaInscrito(usuarioId, cursoId);
        return ResponseEntity.ok(Map.of("inscrito", inscrito));
    }

    @DeleteMapping("/usuario/{usuarioId}/curso/{cursoId}")
    @Operation(summary = "Cancelar inscrição", description = "Remove a inscrição do usuário no curso")
    @ApiResponse(responseCode = "204", description = "Inscrição cancelada")
    @ApiResponse(responseCode = "404", description = "Inscrição não encontrada")
    public ResponseEntity<Void> cancelar(@PathVariable Long usuarioId, @PathVariable Long cursoId) {
        inscricaoService.cancelar(usuarioId, cursoId);
        return ResponseEntity.noContent().build();
    }

    // ---- Progresso de Aulas ----

    @PostMapping("/usuario/{usuarioId}/video/{videoId}/concluir")
    @Operation(summary = "Marcar aula como concluída ou não", description = "Alterna o status de conclusão de uma aula")
    @ApiResponse(responseCode = "200", description = "Progresso atualizado")
    @ApiResponse(responseCode = "404", description = "Usuário ou vídeo não encontrado")
    public ResponseEntity<ProgressoAula> marcarConcluida(
            @PathVariable Long usuarioId,
            @PathVariable Long videoId,
            @RequestBody Map<String, Boolean> body) {
        Boolean concluida = body.getOrDefault("concluida", false);
        return ResponseEntity.ok(progressoService.marcarConcluida(usuarioId, videoId, concluida));
    }

    @PostMapping("/usuario/{usuarioId}/video/{videoId}/anotacao")
    @Operation(summary = "Salvar anotação de uma aula", description = "Salva ou atualiza a anotação do usuário em uma aula")
    @ApiResponse(responseCode = "200", description = "Anotação salva")
    @ApiResponse(responseCode = "404", description = "Usuário ou vídeo não encontrado")
    public ResponseEntity<ProgressoAula> salvarAnotacao(
            @PathVariable Long usuarioId,
            @PathVariable Long videoId,
            @RequestBody Map<String, String> body) {
        String anotacao = body.getOrDefault("anotacao", "");
        return ResponseEntity.ok(progressoService.salvarAnotacao(usuarioId, videoId, anotacao));
    }

    @GetMapping("/usuario/{usuarioId}/curso/{cursoId}/progresso")
    @Operation(summary = "Listar progresso do curso", description = "Retorna o progresso de todas as aulas de um curso para o usuário")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<ProgressoAula>> listarProgresso(@PathVariable Long usuarioId, @PathVariable Long cursoId) {
        return ResponseEntity.ok(progressoService.listarPorUsuarioECurso(usuarioId, cursoId));
    }
}
