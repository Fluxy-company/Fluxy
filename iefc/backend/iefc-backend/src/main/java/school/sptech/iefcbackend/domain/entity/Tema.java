package school.sptech.iefcbackend.domain.entity;

import jakarta.persistence.*;

@Entity(name = "tb_tema")
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tema_id")
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    public Tema() {
    }

    public Tema(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
