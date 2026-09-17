package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_empresa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Empresa {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Column(name = "empresa_id")
    private Long id;

    @Column
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String telefone;

    @ManyToOne
    @JoinColumn
    private Usuario usuario;


}
