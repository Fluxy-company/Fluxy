package school.sptech.iefcbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_curso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long id;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    @JsonBackReference
    private Empresa empresa;
}
