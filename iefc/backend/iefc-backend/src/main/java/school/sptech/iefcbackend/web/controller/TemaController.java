package school.sptech.iefcbackend.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.domain.entity.Tema;
import school.sptech.iefcbackend.usecase.tema.TemaService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/temas")
public class TemaController {

    private final TemaService service;

    public TemaController(TemaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Page<Tema>> listarPaginado(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ALUNO', 'SCOPE_ROLE_COLABORADOR', 'SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Tema> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Tema> criar(@RequestBody Tema tema) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(tema));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Tema> atualizar(@PathVariable Long id, @RequestBody Tema tema) {
        return ResponseEntity.ok(service.atualizar(id, tema));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
