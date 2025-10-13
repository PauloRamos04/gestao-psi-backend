package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private String modulo;
    private String acao;
    private Boolean ativo;
    
    public static PermissionDTO fromEntity(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .nome(permission.getNome())
                .descricao(permission.getDescricao())
                .modulo(permission.getModulo())
                .acao(permission.getAcao())
                .ativo(permission.getAtivo())
                .build();
    }
}


