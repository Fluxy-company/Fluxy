package school.sptech.iefcbackend.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "tb_progresso_aula")
public class ProgressoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progresso_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_video")
    private Video video;

    @Column(name = "concluida")
    private Boolean concluida;

    @Column(name = "anotacao", length = 2000)
    private String anotacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public ProgressoAula() {
    }

    public ProgressoAula(Long id, Usuario usuario, Video video, Boolean concluida, String anotacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.usuario = usuario;
        this.video = video;
        this.concluida = concluida;
        this.anotacao = anotacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
