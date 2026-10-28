package school.sptech.iefcbackend.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.iefcbackend.usecase.auth.AuthService;
import school.sptech.iefcbackend.web.dto.LoginRequestDTO;
import school.sptech.iefcbackend.web.dto.LoginResponseDTO;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Login", description = "Controller para autenticação de usuarios")
@RequestMapping("/api/v1")
public class TokenController {

    private final AuthService authService;

    public TokenController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login", description = "Método que realiza o login dos usuarios", security = {})
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Sem permissão, login incorreto")
    @ApiResponse(responseCode = "500", description = "Erro de servidor / senha invalida")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.autenticar(loginRequest);
        return ResponseEntity.ok(response);
    }
}
