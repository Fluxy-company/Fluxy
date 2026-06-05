package school.sptech.iefcbackend.dto.relatorio;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventoRelatorioDTO {

    private String trimestre;
    private String titulo;
    private String subtitulo;
    private String tipo;
    private String data;
    private String local;
    private String ministrante;
    private String descricao;
    private MultipartFile foto;
}
