package school.sptech.iefcbackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.models.Tema;
import school.sptech.iefcbackend.services.TemaService;

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
    public ResponseEntity<List<Tema>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tema> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Tema> criar(@RequestBody Tema tema) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(tema));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tema> atualizar(@PathVariable Long id, @RequestBody Tema tema) {
        return ResponseEntity.ok(service.atualizar(id, tema));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
