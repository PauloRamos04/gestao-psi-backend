package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetConfirm {
    
    @NotBlank(message = "Token é obrigatório")
    private String token;
    
    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String newPassword;
}

