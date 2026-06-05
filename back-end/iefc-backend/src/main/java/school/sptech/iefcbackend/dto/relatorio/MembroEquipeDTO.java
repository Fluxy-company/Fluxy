package school.sptech.iefcbackend.dto.relatorio;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MembroEquipeDTO {
    private String nome;
    private String cargo;
    private String bio;
    private String categoria;
    private MultipartFile foto;
}
