package school.sptech.fluxybackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.fluxybackend.controllers.dto.UsuarioRequestDTO;
import school.sptech.fluxybackend.controllers.dto.UsuarioResponseDTO;
import school.sptech.fluxybackend.models.Empresa;
import school.sptech.fluxybackend.services.EmpresaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empresas", description = "Controller para salvar e editar dados das empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    @Operation(summary = "Salva os dados da empresa", description = "Método que salva os dados da empresa")
    @ApiResponse(responseCode = "201",
            content = @Content(schema = @Schema(implementation = Empresa.class)),
            description = "Empresa criada com sucesso")
    @ApiResponse(responseCode = "409", description = "Cnpj ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> salvarEmpresa(@RequestBody Empresa empresa){
        empresaService.salvarEmpresa(empresa);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Operation(summary = "Busca todas as empresas", description = "Método que busca todas as empresas")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma empresa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<Empresa>> buscarTodos() {
        return ResponseEntity.ok(empresaService.buscarTodos());
    }

    @GetMapping(value = "/{cnpj}")
    @Operation(summary = "Buscar empresa por Cnpj", description = "Método que busca a empresa pelo cnpj")
    @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Cnpj")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Empresa> acharPeloId(@PathVariable("cnpj") String cnpj){
        return ResponseEntity.ok(empresaService.buscarEmpresaPorCnpj(cnpj));
    }


    @PutMapping(value = "/{id}")
    @Operation(summary = "Edita os dados da empresa pelo Id", description = "Método que edita os dados da empresa pelo Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Empresa> atualizarPorId(@PathVariable Long id, @RequestBody Empresa empresa){
        empresaService.atualizarPorId(id, empresa);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta a empresa pelo Id", description = "Método que deleta a empresa pelo Id")
    @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sem registros nesse Id")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id){
        empresaService.deletarPorId(id);

        return ResponseEntity.noContent().build();
    }

}
