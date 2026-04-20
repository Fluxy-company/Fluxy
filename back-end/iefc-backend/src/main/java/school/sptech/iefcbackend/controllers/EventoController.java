package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.models.Eventos;
import school.sptech.iefcbackend.services.EventoService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "Eventos", description = "Controller para criar, buscar, editar e deletar eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    @Operation(summary = "Para criar eventos", description = "Método que cria os eventos")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Eventos.class)),
    description = "Eventos criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum evento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> criarEvento(@RequestBody Eventos evento){
        eventoService.criarEvento(evento);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/data/{data}")
    @Operation(summary = "Busca eventos pela data", description = "Método que busca os eventos pela data")
    @ApiResponse(responseCode = "200", description = "Eventos retornados com sucesso")
    @ApiResponse(responseCode = "400", description = "Não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Eventos>> buscarPelaData(
            @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
    return ResponseEntity.ok(eventoService.buscarEventoPelaData(data));
        }

    @GetMapping("/status/{status}")
    @Operation(summary = "Busca eventos pela data", description = "Método que busca os eventos pelos status")
    @ApiResponse(responseCode = "200", description = "Eventos retornados com sucesso")
    @ApiResponse(responseCode = "400", description = "Não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Eventos>> buscarPeloStatus(@PathVariable("status") String status){
        return ResponseEntity.ok(eventoService.buscarEventoPeloStatus(status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita o evento pelo id", description = "Método que edita o evento pelo id")
    @ApiResponse(responseCode = "200", description = "video editado com sucesso")
    @ApiResponse(responseCode = "400", description = "Não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Eventos> atualizarEventoPorId(@PathVariable Long id, @RequestBody Eventos eventos){
        return ResponseEntity.ok(eventoService.atualizarPorId(id, eventos));
    }

    @DeleteMapping
    @Operation(summary = "Deletar eventos pelo id", description = "Método que deleta os eventos id")
    @ApiResponse(responseCode = "200", description = "Evento deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem eventos nesse id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id){
        eventoService.deletarPorId(id);

        return ResponseEntity.noContent().build();
    }
}
