package school.sptech.iefcbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

@Entity(name = "tb_curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long id;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "instrutor", length = 100)
    private String instrutor;

    @Formula("(SELECT COUNT(*) FROM video v WHERE v.id_curso = curso_id)")
    private Integer totalAulas;

    @Column(name = "video_id", length = 20)
    private String videoId;

    @ManyToOne
    @JoinColumn(name = "id_tema")
    private Tema tema;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    @JsonBackReference
    private Empresa empresa;

    public Curso() {
    }

    public Curso(Long id, String titulo, String descricao, String instrutor, Integer totalAulas, String videoId, Tema tema, Empresa empresa) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.instrutor = instrutor;
        this.totalAulas = totalAulas;
        this.videoId = videoId;
        this.tema = tema;
        this.empresa = empresa;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(String instrutor) {
        this.instrutor = instrutor;
    }

    public Integer getTotalAulas() {
        return totalAulas;
    }

    public void setTotalAulas(Integer totalAulas) {
        this.totalAulas = totalAulas;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
