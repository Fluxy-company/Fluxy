package school.sptech.iefcbackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.iefcbackend.dto.LoginRequestDTO;
import school.sptech.iefcbackend.models.Role;

import java.util.HashSet;
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

    @Column(length = 250, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tb_usuario_roles", joinColumns = @JoinColumn(name = "id_usuario"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

    // Por padrão todo usuário nasce APROVADO (alunos via autocadastro, e
    // qualquer usuário criado pelo admin via /api/v1/usuarios/admin).
    // Apenas o fluxo de autocadastro de colaborador (seção "cadastro de
    // funcionário") deve sobrescrever isso para PENDENTE.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status = StatusCadastro.APROVADO;

    public boolean loginCorreto(LoginRequestDTO loginRequest, PasswordEncoder passwordEncoder){
        return  passwordEncoder.matches(loginRequest.senha(), this.senha);
    }

    public boolean estaPendente() {
        return this.status == StatusCadastro.PENDENTE;
    }

    public boolean estaReprovado() {
        return this.status == StatusCadastro.REPROVADO;
    }

    public void aprovar() {
        this.status = StatusCadastro.APROVADO;
    }

    public void reprovar() {
        this.status = StatusCadastro.REPROVADO;
    }
}
