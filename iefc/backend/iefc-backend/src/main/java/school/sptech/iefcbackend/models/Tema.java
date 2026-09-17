package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_tema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tema_id")
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
}
