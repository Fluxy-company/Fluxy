package school.sptech.iefcbackend.domain.port;

import school.sptech.iefcbackend.web.dto.relatorio.RelatorioRequestDTO;

public interface GeradorRelatorioPdfPort {

    byte[] gerarPdf(RelatorioRequestDTO dados);
}
