package school.sptech.fluxybackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.fluxybackend.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO dto){
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto){
        return ResponseEntity.ok (service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
