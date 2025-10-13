package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private Boolean sistema;
    private Set<Long> permissionIds;
    private Set<PermissionDTO> permissions;
    
    public static RoleDTO fromEntity(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .nome(role.getNome())
                .descricao(role.getDescricao())
                .ativo(role.getAtivo())
                .sistema(role.getSistema())
                .permissionIds(role.getPermissions().stream()
                        .map(p -> p.getId())
                        .collect(Collectors.toSet()))
                .permissions(role.getPermissions().stream()
                        .map(PermissionDTO::fromEntity)
                        .collect(Collectors.toSet()))
                .build();
    }
}


