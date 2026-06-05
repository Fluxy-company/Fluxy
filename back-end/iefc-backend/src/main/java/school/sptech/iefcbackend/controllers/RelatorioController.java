package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.dto.relatorio.RelatorioRequestDTO;
import school.sptech.iefcbackend.services.RelatorioService;

import javax.management.relation.RelationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/relatorio")
@Tag(name = "Relatório PDF", description = "Gera o Relatório de Atividades IEFC em PDF")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @PostMapping(
            value = "/gerar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    @Operation(
            summary = "Gerar Relatório PDF",
            description = "Recebe os dados do relatório via multipart/form-data e retorna um PDF"
    )
    public ResponseEntity<byte[]> gerarRelatorio(
            @ModelAttribute RelatorioRequestDTO dto
    ) {
        byte[] pdf = relatorioService.gerarPdf(dto);

        String nomeArquivo = "Relatorio_IEFC_" + (dto.getAno() != null ? dto.getAno() : "2025") + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", nomeArquivo);
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
