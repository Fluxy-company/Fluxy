package school.sptech.emailservice.infrastructure.web.mapper;

import school.sptech.emailservice.domain.model.Email;
import school.sptech.emailservice.domain.model.Pagina;
import school.sptech.emailservice.domain.port.in.SolicitarEnvioEmailCommand;
import school.sptech.emailservice.infrastructure.web.dto.EmailResponseDTO;
import school.sptech.emailservice.infrastructure.web.dto.PaginaResponseDTO;
import school.sptech.emailservice.infrastructure.web.dto.SolicitarEnvioEmailRequestDTO;

import java.util.List;

public final class EmailWebMapper {

    private EmailWebMapper() {
    }

    public static SolicitarEnvioEmailCommand toCommand(SolicitarEnvioEmailRequestDTO dto) {
        return new SolicitarEnvioEmailCommand(
                dto.destinatarios(),
                dto.copia(),
                dto.remetente(),
                dto.assunto(),
                dto.corpo()
        );
    }

    public static EmailResponseDTO toResponseDTO(Email email) {
        return new EmailResponseDTO(
                email.getId(),
                email.getDestinatarios(),
                email.getCopia(),
                email.getRemetente(),
                email.getAssunto(),
                email.getStatus(),
                email.getTentativas(),
                email.getMaxTentativas(),
                email.getCriadoEm(),
                email.getAtualizadoEm(),
                email.getEnviadoEm(),
                email.getMensagemErro()
        );
    }

    public static PaginaResponseDTO<EmailResponseDTO> toPaginaDTO(Pagina<Email> pagina) {
        List<EmailResponseDTO> conteudo = pagina.conteudo().stream()
                .map(EmailWebMapper::toResponseDTO)
                .toList();

        int totalPaginas = pagina.tamanho() == 0
                ? 0
                : (int) Math.ceil((double) pagina.totalElementos() / pagina.tamanho());

        return new PaginaResponseDTO<>(conteudo, pagina.pagina(), pagina.tamanho(), pagina.totalElementos(), totalPaginas);
    }
}
