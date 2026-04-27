package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "tb_eventos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column
    private String status;

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

}
