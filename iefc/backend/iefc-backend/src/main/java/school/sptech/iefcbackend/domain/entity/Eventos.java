package school.sptech.iefcbackend.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "tb_eventos")
public class Eventos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventos_id")
    private Long id;

    @Column
    private String titulo;

    @Column
    private LocalDate data;

    @Column
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Eventos.StatusEventos status;

    public enum StatusEventos {
        ATIVO, FECHADO, CANCELADO, EM_ANALISE
    }

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
            name = "usuario_evento",
            joinColumns = @JoinColumn(name = "idEvento"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario")
    )
    private List<Usuario> usuarios;

    public Eventos() {
    }

    public Eventos(Long id, String titulo, LocalDate data, String descricao, StatusEventos status, Empresa empresa, List<Usuario> usuarios) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.status = status;
        this.empresa = empresa;
        this.usuarios = usuarios;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusEventos getStatus() {
        return status;
    }

    public void setStatus(StatusEventos status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
