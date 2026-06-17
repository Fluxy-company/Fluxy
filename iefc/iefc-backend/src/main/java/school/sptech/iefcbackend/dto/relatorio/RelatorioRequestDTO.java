package school.sptech.iefcbackend.dto.relatorio;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RelatorioRequestDTO {

    private String ano;
    private String depoimentoDiretoraPresidente;
    private String depoimentoDiretoraOperacional;
    private MultipartFile fotoDiretoraPres;
    private MultipartFile fotoDiretoraOp;
    private String estruturaOrganizacional;
    private String introducaoSobreIefc;
    private String pilarEducacao;
    private List<MembroEquipeDTO> equipe;
    private List<EventoRelatorioDTO> eventos;
    private String textoLumina;
    private String textoPilarPesquisa;
    private String textoPesquisaBloco2;
    private String totalEventos;
    private String participantesDiretos;
    private String beneficiariosIndiretos;
    private List<DepoimentoDTO> depoimentos;
    private String textoPresencaDigital;
    private String textoParceiras;
    private String transparenciaBloco1;
    private String transparenciaBloco2;
    private String consideracoesBloco1;
    private String consideracoesBloco2;
}