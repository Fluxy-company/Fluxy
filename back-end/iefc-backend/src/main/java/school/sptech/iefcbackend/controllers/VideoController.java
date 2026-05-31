package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.models.Video;
import school.sptech.iefcbackend.services.VideoService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/videos")
@Tag(name = "Video", description = "Controller para salvar, editar e deletar os dados videos.")
public class VideoController {

    @Autowired
    private VideoService service;

    @GetMapping
    @Operation(summary = "Busca todos os videos", description = "Método que busca todos os videos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Video>> listarTodos(){
        return ResponseEntity.ok(service.buscarTodos());
    }

    @PostMapping
    @Operation(summary = "Para salvar videos", description = "Método para salvar videos")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Video.class)),
            description = "Video salvo com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum video encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> salvarVideo(@RequestBody Video video){
        service.salvarVideo(video);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita video pelo id", description = "Método para editar os dados do video")
    @ApiResponse(responseCode = "200", description = "Video editado com sucesso")
    @ApiResponse(responseCode = "400", description = "Não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Video> atualizarEventoPorId(@PathVariable Long id, @RequestBody Video video){
        return ResponseEntity.ok(service.atualizarPeloId(id, video));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar video pelo id", description = "Método que deleta os videos pelo id")
    @ApiResponse(responseCode = "204", description = "Video deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem video nesse id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id){
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
