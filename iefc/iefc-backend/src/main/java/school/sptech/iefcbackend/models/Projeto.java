package school.sptech.iefcbackend.models;

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
    @Column(name = "projeto_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    public enum StatusProjeto {
        EM_ANDAMENTO, CONCLUIDO, CANCELADO, PENDENTE
    }

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public enum StatusProjeto {
        EM_ANDAMENTO, CONCLUIDO, CANCELADO, PENDENTE
    }
}
