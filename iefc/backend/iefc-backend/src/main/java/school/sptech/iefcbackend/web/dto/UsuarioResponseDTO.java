package school.sptech.iefcbackend.web.dto;

import school.sptech.iefcbackend.domain.enums.Role;
import school.sptech.iefcbackend.domain.enums.StatusCadastro;

import java.util.Set;

public class UsuarioResponseDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private Set<Role> roles;
    private StatusCadastro status;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long idUsuario, String nome, String email, Set<Role> roles, StatusCadastro status) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.roles = roles;
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public StatusCadastro getStatus() {
        return status;
    }

    public void setStatus(StatusCadastro status) {
        this.status = status;
    }
}
