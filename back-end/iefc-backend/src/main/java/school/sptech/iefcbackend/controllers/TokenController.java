package school.sptech.iefcbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.controllers.dto.LoginRequestDTO;
import school.sptech.iefcbackend.controllers.dto.LoginResponseDTO;
import school.sptech.iefcbackend.models.Role;
import school.sptech.iefcbackend.repository.UsuarioRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@Getter
@Setter
@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Login", description = "Controller para autenticação de usuarios")
@RequestMapping("/api/v1")
public class TokenController {

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
        var usuario = usuarioRepository.findByEmail(loginRequest.email());
        if(usuario.isEmpty() || !usuario.get().loginCorreto(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("Usuario ou senha invalido!");
        }

        var agora = Instant.now();

        var expiraEm = 300L;

        var escopo = usuario.get().getRoles()
                .stream()
                .map(Role::getNome)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("iefcbackend")
                .subject(usuario.get().getId().toString())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expiraEm))
                .claim("escopo", escopo)
                .build();

        var jwtValor = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return  ResponseEntity.ok(new LoginResponseDTO(jwtValor, expiraEm));
    }
}
