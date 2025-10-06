package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.LoginRequest;
import com.gestaopsi.prd.dto.LoginResponse;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.service.AuthService;
import com.gestaopsi.prd.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuarioOpt = authService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                UserDetails userDetails = User.builder()
                        .username(usuario.getUsername())
                        .password(usuario.getSenha())
                        .authorities(new ArrayList<>())
                        .build();

                String token = jwtService.generateToken(userDetails);

                LoginResponse response = new LoginResponse();
                response.setToken(token);
                response.setUserId(usuario.getId());
                response.setUsername(usuario.getUsername());
                // Evitar NPE: ler IDs diretamente das relações com null-check
                response.setClinicaId(
                        usuario.getClinica() != null ? usuario.getClinica().getId() : null
                );
                response.setPsicologId(
                        usuario.getPsicologo() != null ? usuario.getPsicologo().getId() : null
                );
                response.setTipoUser(
                        usuario.getTipo() != null ? String.valueOf(usuario.getTipo().getId()) : null
                );
                response.setClinicaNome(
                        usuario.getClinica() != null ? usuario.getClinica().getNome() : null
                );
                response.setPsicologoNome(
                        usuario.getPsicologo() != null ? usuario.getPsicologo().getNome() : null
                );
                response.setTituloSite(usuario.getTitulo());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            log.error("Erro ao realizar login", e);
            return ResponseEntity.status(500).build();
        }
    }
}


