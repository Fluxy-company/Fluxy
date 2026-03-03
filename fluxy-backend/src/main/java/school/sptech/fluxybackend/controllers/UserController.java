package school.sptech.fluxybackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.fluxybackend.dto.UserRequestDTO;
import school.sptech.fluxybackend.dto.UserResponseDTO;
import school.sptech.fluxybackend.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dto){
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO dto){
        return ResponseEntity.ok (service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
