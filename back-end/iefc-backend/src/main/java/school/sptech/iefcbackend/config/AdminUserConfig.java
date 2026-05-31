package school.sptech.iefcbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.models.Usuario;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        usuarioRepository.findByEmail("admin@admin.com").ifPresentOrElse(
                user -> System.out.println("Admin já existe, pulando criação."),
                () -> {
                    var admin = new Usuario();
                    admin.setNome("Admin");
                    admin.setSobrenome("Administrador");
                    admin.setEmail("admin@admin.com");
                    admin.setSenha(passwordEncoder.encode("Admin@123"));
                    admin.setRoles(Set.of(Role.ADMIN));
                    usuarioRepository.save(admin);
                    System.out.println("Admin criado com sucesso.");
                }
        );

        usuarioRepository.findByEmail("user@user.com").ifPresentOrElse(
                user -> System.out.println("User já existe, pulando criação."),
                () -> {
                    var basicUser = new Usuario();
                    basicUser.setNome("User");
                    basicUser.setSobrenome("Padrão");
                    basicUser.setEmail("user@user.com");
                    basicUser.setSenha(passwordEncoder.encode("User@123"));
                    basicUser.setRoles(Set.of(Role.BASIC));
                    usuarioRepository.save(basicUser);
                    System.out.println("User criado com sucesso.");
                }
        );
    }
}
