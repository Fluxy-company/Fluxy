package school.sptech.iefcbackend.web.dto.relatorio;

public class DepoimentoDTO {

    private String texto;

    public DepoimentoDTO() {
    }

    public DepoimentoDTO(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
