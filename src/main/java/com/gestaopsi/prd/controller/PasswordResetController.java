package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.PasswordResetConfirm;
import com.gestaopsi.prd.dto.PasswordResetRequest;
import com.gestaopsi.prd.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/password-reset")
@RequiredArgsConstructor
@Tag(name = "Recuperação de Senha", description = "Endpoints para recuperação de senha")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    @Operation(summary = "Solicitar recuperação de senha", description = "Envia email com link de recuperação")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.createPasswordResetToken(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Se o email existir, um link de recuperação será enviado"));
    }

    @PostMapping("/reset")
    @Operation(summary = "Confirmar reset de senha", description = "Redefine a senha usando o token recebido")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody PasswordResetConfirm request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token", description = "Verifica se o token é válido")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = passwordResetService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}

