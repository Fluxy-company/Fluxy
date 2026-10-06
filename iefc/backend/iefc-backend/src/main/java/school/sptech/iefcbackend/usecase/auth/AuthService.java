package school.sptech.iefcbackend.usecase.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import school.sptech.iefcbackend.domain.enums.Role;
import school.sptech.iefcbackend.domain.exception.CadastroPendenteException;
import school.sptech.iefcbackend.domain.exception.CadastroReprovadoException;
import school.sptech.iefcbackend.domain.port.UsuarioRepositoryPort;
import school.sptech.iefcbackend.web.dto.LoginRequestDTO;
import school.sptech.iefcbackend.web.dto.LoginResponseDTO;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(JwtEncoder jwtEncoder,
                       UsuarioRepositoryPort usuarioRepositoryPort,
                       BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO loginRequest) {
        var usuario = usuarioRepositoryPort.findByEmail(loginRequest.email())
                .filter(user -> user.loginCorreto(loginRequest, passwordEncoder))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha invalidos."));

        if (usuario.estaPendente()) {
            throw new CadastroPendenteException(
                    "Seu cadastro ainda está aguardando aprovação do administrador.");
        }
        if (usuario.estaReprovado()) {
            throw new CadastroReprovadoException(
                    "Seu cadastro não foi aprovado. Entre em contato com o administrador.");
        }

        var agora = Instant.now();
        var expiraEm = 3600L;

        var escopo = usuario.getRoles()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("iefcbackend")
                .subject(usuario.getId().toString())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expiraEm))
                .claim("scope", escopo)
                .build();

        var jwtValor = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValor, expiraEm);
    }
}
