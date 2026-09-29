package school.sptech.iefcbackend.web.dto.relatorio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

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

    @JsonIgnore
    private String fotoBase64;

    public EventoRelatorioDTO() {
    }

    public EventoRelatorioDTO(String trimestre, String titulo, String subtitulo, String tipo, String data, String local, String ministrante, String descricao, MultipartFile foto, String fotoBase64) {
        this.trimestre = trimestre;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.tipo = tipo;
        this.data = data;
        this.local = local;
        this.ministrante = ministrante;
        this.descricao = descricao;
        this.foto = foto;
        this.fotoBase64 = fotoBase64;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMinistrante() {
        return ministrante;
    }

    public void setMinistrante(String ministrante) {
        this.ministrante = ministrante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public MultipartFile getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {
        this.foto = foto;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}
