package school.sptech.iefcbackend.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.usecase.inscricao.MeusCursosService;
import school.sptech.iefcbackend.web.dto.MeuCursoResponseDTO;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Meus cursos", description = "Cursos em que o usuário está inscrito, com progresso")
public class MeusCursosController {

    private final MeusCursosService service;

    public MeusCursosController(MeusCursosService service) {
        this.service = service;
    }

    @GetMapping("/{usuarioId}/meus-cursos")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Lista os cursos do usuário com progresso", description = "Retorna inscrições com total de aulas, aulas concluídas e última aula assistida. Só o próprio usuário ou um ADMIN.")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<List<MeuCursoResponseDTO>> listar(@PathVariable Long usuarioId,
                                                            Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("SCOPE_ROLE_ADMIN"));
        if (!admin && !authentication.getName().equals(String.valueOf(usuarioId))) {
            throw new AccessDeniedException("Você só pode ver os seus próprios cursos");
        }
        return ResponseEntity.ok(service.listar(usuarioId));
    }
}
