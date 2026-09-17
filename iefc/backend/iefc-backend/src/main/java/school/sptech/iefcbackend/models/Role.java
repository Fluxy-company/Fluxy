package school.sptech.iefcbackend.models;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    COLABORADOR("ROLE_COLABORADOR"),
    ALUNO("ROLE_ALUNO");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public static Role fromAuthority(String authority) {
        for (Role role : values()) {
            if (role.authority.equalsIgnoreCase(authority))
                return role;
        }
        throw new IllegalArgumentException("Role desconhecida: " + authority);
    }
}
