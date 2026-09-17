package school.sptech.emailservice.infrastructure.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.emailservice.domain.model.EmailStatus;
import school.sptech.emailservice.domain.port.in.ConsultarEmailUseCase;
import school.sptech.emailservice.domain.port.in.ReenviarEmailUseCase;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailUseCase;
import school.sptech.emailservice.infrastructure.web.dto.EmailResponseDTO;
import school.sptech.emailservice.infrastructure.web.dto.PaginaResponseDTO;
import school.sptech.emailservice.infrastructure.web.dto.SolicitarEnvioEmailRequestDTO;
import school.sptech.emailservice.infrastructure.web.mapper.EmailWebMapper;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/emails")
@Tag(name = "Emails", description = "Envio assincrono de e-mails via Mailtrap/JavaMail")
public class EmailController {

    private final SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase;
    private final ConsultarEmailUseCase consultarEmailUseCase;
    private final ReenviarEmailUseCase reenviarEmailUseCase;

    public EmailController(SolicitarEnvioEmailUseCase solicitarEnvioEmailUseCase,
                            ConsultarEmailUseCase consultarEmailUseCase,
                            ReenviarEmailUseCase reenviarEmailUseCase) {
        this.solicitarEnvioEmailUseCase = solicitarEnvioEmailUseCase;
        this.consultarEmailUseCase = consultarEmailUseCase;
        this.reenviarEmailUseCase = reenviarEmailUseCase;
    }

    @PostMapping
    @Operation(summary = "Solicita o envio assincrono de um e-mail",
            description = "Persiste o e-mail com status PENDENTE e retorna imediatamente. "
                    + "O envio efetivo acontece em background (fila assincrona).")
    @ApiResponse(responseCode = "202", description = "E-mail enfileirado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada invalidos")
    @ApiResponse(responseCode = "401", description = "API key ausente ou invalida")
    public ResponseEntity<EmailResponseDTO> solicitarEnvio(@Valid @RequestBody SolicitarEnvioEmailRequestDTO dto) {
        var email = solicitarEnvioEmailUseCase.solicitar(EmailWebMapper.toCommand(dto));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(EmailWebMapper.toResponseDTO(email));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta o status de um e-mail pelo id")
    @ApiResponse(responseCode = "200", description = "E-mail encontrado")
    @ApiResponse(responseCode = "404", description = "Nenhum e-mail encontrado para o id informado")
    public ResponseEntity<EmailResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(EmailWebMapper.toResponseDTO(consultarEmailUseCase.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Lista e-mails, com filtro opcional por status", description = "Paginado; use os "
            + "parametros 'pagina' e 'tamanho' para navegar entre os resultados.")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<PaginaResponseDTO<EmailResponseDTO>> listar(
            @RequestParam(required = false) EmailStatus status,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        return ResponseEntity.ok(EmailWebMapper.toPaginaDTO(consultarEmailUseCase.listar(status, pagina, tamanho)));
    }

    @PostMapping("/{id}/reenviar")
    @Operation(summary = "Reenfileira manualmente um e-mail que falhou",
            description = "Somente e-mails com status FALHA podem ser reenviados por este endpoint.")
    @ApiResponse(responseCode = "202", description = "E-mail reenfileirado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum e-mail encontrado para o id informado")
    @ApiResponse(responseCode = "400", description = "O e-mail nao esta em um estado que permita reenvio")
    public ResponseEntity<EmailResponseDTO> reenviar(@PathVariable UUID id) {
        return ResponseEntity.accepted().body(EmailWebMapper.toResponseDTO(reenviarEmailUseCase.reenviar(id)));
    }
}
