package school.sptech.iefcbackend.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.iefcbackend.domain.enums.Role;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;
import school.sptech.iefcbackend.web.dto.LoginRequestDTO;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "tb_usuario")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status = StatusCadastro.APROVADO;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, Set<Role> roles, Long createdAt, StatusCadastro status) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roles = roles;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public StatusCadastro getStatus() {
        return status;
    }

    public void setStatus(StatusCadastro status) {
        this.status = status;
    }

    public boolean loginCorreto(LoginRequestDTO loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
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
