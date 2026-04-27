package school.sptech.fluxybackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import school.sptech.fluxybackend.models.Projeto;
import school.sptech.fluxybackend.services.ProjetoService;
import java.sql.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/projetos")
@Tag(name = "Projetos", description = "Controller para salvar e editar os dados dos projetos.")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    @Operation(summary = "Salvar os dados do projeto", description = "Método que salva os dados do projeto")
    @ApiResponse(responseCode = "201",
            content = @Content(schema = @Schema(implementation = Projeto.class)),
            description = "Projeto foi criado com sucesso!")
    @ApiResponse(responseCode = "409", description = "Já existe um projeto cadastrado com estes dados.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> salvarProjeto(@RequestBody Projeto projeto){
        projetoService.salvarProjeto(projeto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "Buscar todos os projetos", description = "Método que busca todos os projetos.")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    public ResponseEntity<List<Projeto>> buscarTodos() {
        return ResponseEntity.ok(projetoService.buscarTodos());
    }

    @GetMapping(value = "/nome/{nome}")
    @Operation(summary = "Buscar projeto pelo nome", description = "Método que busca o projeto pelo nome.")
    @ApiResponse(responseCode = "200", description = "Projeto foi encontrado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi encontrado com este nome.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    public ResponseEntity<Projeto> pesquisarProjetoPeloNome(@PathVariable("nome") String nome){
        return ResponseEntity.ok(projetoService.buscarProjetoPorNome(nome));
    }

    @GetMapping(value = "/dataInicio/{data}")
    @Operation(summary = "Buscar projeto pela data de início", description = "Método que busca o projeto pela data de início informada.")
    @ApiResponse(responseCode = "200", description = "Projeto foi encontrado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi iniciado na data informada.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    public ResponseEntity<List<Projeto>> pesquisarProjetoPelaDataInicio(
            @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data){
        return ResponseEntity.ok(projetoService.buscarProjetoPorDataInicio(data));
    }

    @GetMapping(value = "/status/{status}")
    @Operation(summary = "Buscar projeto pelo status", description = "Método que busca um projeto pelo status")
    @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi encontrado com esse status")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    public ResponseEntity<List<Projeto>> pesquisarProjetoPeloStatus(@PathVariable("status") String status){
        return ResponseEntity.ok(projetoService.buscarProjetoPorStatus(status));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Alterar os dados do projeto pelo ID", description = "Método que altera os dados do projeto pelo ID")
    @ApiResponse(responseCode = "200", description = "Projeto alterado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi encontrado com essa identificação.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    public ResponseEntity<Projeto> atualizarProjetoPorId(@PathVariable Long id, @RequestBody Projeto projeto){
        return ResponseEntity.ok(projetoService.atualizarPorId(id, projeto));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Exclui o projeto pelo ID", description = "Método que exclui um projeto pelo ID")
    @ApiResponse(responseCode = "204", description = "Projeto excluído com sucesso!")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto foi encontrado com essa identificação.")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id){
        projetoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
