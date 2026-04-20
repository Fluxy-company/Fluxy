package school.sptech.iefcbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.RoleRepository;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.util.Set;

@Configuration
@Getter
@Setter
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;

    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByNome(Role.Valores.ADMIN.name());

        var usuarioAdmin = usuarioRepository.findByEmail("admin@admin.com");

        usuarioAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("adm ja existe");
                },
                () -> {
                    var usuario = new Usuario();
                    usuario.setNome("admin");
                    usuario.setEmail("admin@admin.com");
                    usuario.setSenha(passwordEncoder.encode("senha123"));
                    usuario.setRoles(Set.of(roleAdmin));
                    usuarioRepository.save(usuario);
                });
    }
}
