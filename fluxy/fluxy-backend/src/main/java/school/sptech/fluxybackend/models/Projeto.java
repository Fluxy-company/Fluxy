package school.sptech.fluxybackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    public enum StatusProjeto {
        EM_ANDAMENTO, CONCLUIDO, CANCELADO, PENDENTE
    }
}
