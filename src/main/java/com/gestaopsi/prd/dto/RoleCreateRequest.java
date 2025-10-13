package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {
    
    @NotBlank(message = "Nome da role é obrigatório")
    private String nome;
    
    private String descricao;
    
    @NotNull(message = "Status ativo é obrigatório")
    @Builder.Default
    private Boolean ativo = true;
    
    @Builder.Default
    private Set<Long> permissionIds = new HashSet<>();
}

