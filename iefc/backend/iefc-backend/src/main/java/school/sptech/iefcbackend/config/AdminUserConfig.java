package school.sptech.iefcbackend.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger log = LoggerFactory.getLogger(AdminUserConfig.class);

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.senha}")
    private String adminSenha;

    @Value("${app.usuario-padrao.email:}")
    private String usuarioPadraoEmail;

    @Value("${app.usuario-padrao.senha:}")
    private String usuarioPadraoSenha;

    @Override
    public void run(String... args) {
        criarAdmin();
        criarUsuarioPadrao();
    }

    private void criarAdmin() {
        if (adminSenha == null || adminSenha.isBlank()) {
            throw new IllegalStateException(
                    "Usuario admin não configurado. Defina APP_ADMIN_EMAIL e APP_ADMIN_SENHA.");
        }

        usuarioRepository.findByEmail(adminEmail).ifPresentOrElse(
                user -> log.info("Admin já existe, pulando criação."),
                () -> {
                    var admin = new Usuario();
                    admin.setNome("Admin da silva");
                    admin.setEmail(adminEmail);
                    admin.setSenha(passwordEncoder.encode(adminSenha));
                    admin.setRoles(Set.of(Role.ADMIN));
                    usuarioRepository.save(admin);
                    log.info("Admin criado com sucesso.");
                });
    }

    private void criarUsuarioPadrao() {
        if (usuarioPadraoEmail.isBlank() || usuarioPadraoSenha.isBlank()) {
            log.warn("Usuario padrão não configurado (APP_USUARIO_PADRAO_EMAIL/SENHA ausentes) - pulando criação.");
            return;
        }

        usuarioRepository.findByEmail(usuarioPadraoEmail).ifPresentOrElse(
                user -> log.info("Usuario padrão já existe, pulando criação."),
                () -> {
                    var basicUser = new Usuario();
                    basicUser.setNome("Usuário Padrão");
                    basicUser.setEmail(usuarioPadraoEmail);
                    basicUser.setSenha(passwordEncoder.encode(usuarioPadraoSenha));
                    basicUser.setRoles(Set.of(Role.ALUNO));
                    usuarioRepository.save(basicUser);
                    log.info("Usuario padrão criado com sucesso.");
                });
    }
}