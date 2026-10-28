package school.sptech.iefcbackend.usecase.relatorio;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.event.RelatorioGeradoEvent;
import school.sptech.iefcbackend.domain.port.GeradorRelatorioPdfPort;
import school.sptech.iefcbackend.web.dto.relatorio.RelatorioRequestDTO;

@Service
public class RelatorioService {

    private final GeradorRelatorioPdfPort geradorRelatorioPdfPort;
    private final ApplicationEventPublisher eventPublisher;

    public RelatorioService(GeradorRelatorioPdfPort geradorRelatorioPdfPort,
                            ApplicationEventPublisher eventPublisher) {
        this.geradorRelatorioPdfPort = geradorRelatorioPdfPort;
        this.eventPublisher = eventPublisher;
    }

    public byte[] gerarPdf(RelatorioRequestDTO dto) {
        byte[] pdfBytes = geradorRelatorioPdfPort.gerarPdf(dto);

        String anoRelatorio = (dto.getAno() != null ? dto.getAno() : "2026");
        eventPublisher.publishEvent(new RelatorioGeradoEvent(this, anoRelatorio, pdfBytes.length));

        return pdfBytes;
    }
}