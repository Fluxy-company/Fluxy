package school.sptech.iefcbackend.dto.relatorio;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RelatorioRequestDTO {

    private String ano;

    private String introducao;
    private String missao;
    private String proposito;
    private String depoimentoDiretoraPresidente;
    private String depoimentoDiretoraOperacional;
    private MultipartFile fotoDiretoraPres;
    private MultipartFile fotoDiretoraOp;

    private List<MembroEquipeDTO> equipe;

    private String introAtividades;
    private List<EventoRelatorioDTO> eventos;

    private Integer totalEventos;
    private Integer participantesDirectos;
    private String beneficiariosIndiretos;
    private List<DepoimentoDTO> depoimentos;

    private String textoParceiras;
    private List<String> empresasParceiras;

    private String textoTransparencia;

    private String consideracoesFinais;
}
