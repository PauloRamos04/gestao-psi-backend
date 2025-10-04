package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.LoginRequest;
import com.gestaopsi.prd.dto.LoginResponse;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.service.AuthService;
import com.gestaopsi.prd.service.JwtService;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;

    public AuthController(AuthService authService, JwtService jwtService, ClinicaRepository clinicaRepository, PsicologoRepository psicologoRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.clinicaRepository = clinicaRepository;
        this.psicologoRepository = psicologoRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuarioOpt = authService.authenticate(
                loginRequest.getClinicaLogin(),
                loginRequest.getPsicologLogin(),
                loginRequest.getPassword()
            );

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                UserDetails userDetails = User.builder()
                        .username(usuario.getClinicaId() + ":" + usuario.getPsicologId())
                        .password(usuario.getSenha())
                        .authorities(new ArrayList<>())
                        .build();

                String token = jwtService.generateToken(userDetails);

                LoginResponse response = new LoginResponse();
                response.setToken(token);
                response.setUserId(usuario.getId());
                response.setClinicaId(usuario.getClinicaId().longValue());
                response.setPsicologId(usuario.getPsicologId().longValue());
                response.setClinicaLogin(loginRequest.getClinicaLogin());
                response.setPsicologLogin(loginRequest.getPsicologLogin());
                response.setTipoUser(String.valueOf(usuario.getTipoId()));
                response.setClinicaNome(
                        clinicaRepository.findByClinicaLoginAndStatusTrue(loginRequest.getClinicaLogin())
                                .map(c -> c.getNome())
                                .orElse(null)
                );
                response.setPsicologNome(
                        psicologoRepository.findByPsicologLogin(loginRequest.getPsicologLogin())
                                .map(p -> p.getNome())
                                .orElse(null)
                );
                response.setTituloSite(usuario.getTitulo());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}


