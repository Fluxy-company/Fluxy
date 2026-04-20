package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.iefcbackend.controllers.dto.LoginRequestDTO;

import java.util.Set;

@Entity(name = "tb_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(length = 50)
    private String sobrenome;

    @Column(length = 100, nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String senha;

@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
@JoinTable(
        joinColumns = @JoinColumn,
        inverseJoinColumns = @JoinColumn
)
    private Set<Role> roles;

    @Column(nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

    public boolean loginCorreto(LoginRequestDTO loginRequest, PasswordEncoder passwordEncoder){
        return  passwordEncoder.matches(loginRequest.senha(), this.senha);
    }
}
