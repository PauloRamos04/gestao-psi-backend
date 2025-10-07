package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.LoginRequest;
import com.gestaopsi.prd.dto.LoginResponse;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.service.AuthService;
import com.gestaopsi.prd.service.JwtService;
import com.gestaopsi.prd.service.LogAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final LogAuditoriaService logAuditoriaService;
    private final com.gestaopsi.prd.repository.UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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

                // Registrar log de login bem-sucedido
                logAuditoriaService.registrarLog(
                    "LOGIN",
                    "Usuario",
                    usuario.getId(),
                    "Login realizado com sucesso: " + usuario.getUsername()
                );

                return ResponseEntity.ok(response);
            } else {
                // Registrar tentativa de login falhada
                logAuditoriaService.registrarLog(
                    "LOGIN_FAILED",
                    "Usuario",
                    null,
                    "Tentativa de login falhada para: " + loginRequest.getUsername()
                );
                
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            log.error("Erro ao realizar login", e);
            
            // Registrar erro no log
            logAuditoriaService.registrarErro(
                "Erro ao processar login: " + loginRequest.getUsername(),
                e
            );
            
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Alterar senha do usuário autenticado")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) {
        Usuario usuario = usuarioRepository.findByUsernameAndStatusTrue(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), usuario.getSenha())) {
            return ResponseEntity.badRequest().body("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(req.getNewPassword()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ChangePasswordRequest {
        private String username; // opcional: se tiver SecurityContext, pode ser omitido
        private String currentPassword;
        private String newPassword;
    }
}


