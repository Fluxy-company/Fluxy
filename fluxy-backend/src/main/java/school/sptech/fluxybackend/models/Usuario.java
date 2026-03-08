package school.sptech.fluxybackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Usuario {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String sobrenome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

}
