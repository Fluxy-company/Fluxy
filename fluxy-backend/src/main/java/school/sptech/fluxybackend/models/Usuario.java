package school.sptech.fluxybackend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.fluxybackend.controllers.dto.LoginRequestDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 50, nullable = false)
    private String nome;
    @Column(length = 50)
    private String sobrenome;

    @Column(length = 100, nullable = false, unique = true)
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

    public Usuario() {
    }

    public Usuario(Long idUsuario, String nome, String sobrenome, String email, String senha, Set<Role> roles, Long createdAt) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
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
}
