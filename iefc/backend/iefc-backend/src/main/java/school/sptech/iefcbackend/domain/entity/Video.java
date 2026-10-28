package school.sptech.iefcbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVideo")
    private Long id;

    private String titulo;

    private String url;

    @Column(name = "video_id", length = 20)
    private String videoId;

    @Column(name = "duracao", length = 10)
    private String duracao;

    @Column(name = "modulo", length = 100)
    private String modulo;

    @Column(name = "ordem")
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "idCurso")
    @JsonBackReference
    private Curso curso;

    public Video() {
    }

    public Video(Long id, String titulo, String url, String videoId, String duracao, String modulo, Integer ordem, Curso curso) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.videoId = videoId;
        this.duracao = duracao;
        this.modulo = modulo;
        this.ordem = ordem;
        this.curso = curso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
