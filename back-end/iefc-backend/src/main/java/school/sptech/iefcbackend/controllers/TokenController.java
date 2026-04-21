package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.dto.LoginRequestDTO;
import school.sptech.iefcbackend.dto.LoginResponseDTO;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Login", description = "Controller para autenticação de usuarios")
@RequestMapping("/api/v1")
public class TokenController {

    private static final long TOKEN_EXPIRATION_SECONDS = 3600L;

    private final JwtEncoder jwtEncoder;

    private final UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(BCryptPasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, JwtEncoder jwtEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.jwtEncoder = jwtEncoder;
    }

    @Operation(summary = "Login", description = "Método que realiza o login dos usuarios")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Sem permissão, login incorreto")
    @ApiResponse(responseCode = "500", description = "Erro de servidor / senha invalida")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest){
        var usuario = usuarioRepository.findByEmail(loginRequest.email())
                .filter(user -> user.loginCorreto(loginRequest, passwordEncoder))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha invalidos."));

        var agora = Instant.now();

        var escopo = usuario.getRoles()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("iefcbackend")
                .subject(usuario.getId().toString())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(TOKEN_EXPIRATION_SECONDS))
                .claim("escopo", escopo)
                .build();

        var jwtValor = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return  ResponseEntity.ok(new LoginResponseDTO(jwtValor, TOKEN_EXPIRATION_SECONDS));
    }
}
