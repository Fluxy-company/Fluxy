package school.sptech.fluxybackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "tb_projeto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjeto;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column
    private String descricao;

    @Column
    private Date dataInicio;

    @Column
    private Date dataFim;

    @Column
    private String status;

}
