package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.RoleCreateRequest;
import com.gestaopsi.prd.dto.RoleDTO;
import com.gestaopsi.prd.entity.Permission;
import com.gestaopsi.prd.entity.Role;
import com.gestaopsi.prd.repository.PermissionRepository;
import com.gestaopsi.prd.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    // Carregar permissões para cada role
                    Role roleWithPermissions = roleRepository.findByIdWithPermissions(role.getId()).orElse(role);
                    return RoleDTO.fromEntity(roleWithPermissions);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> getActiveRoles() {
        return roleRepository.findAllActiveWithPermissions().stream()
                .map(RoleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        return RoleDTO.fromEntity(role);
    }

    @Transactional(readOnly = true)
    public RoleDTO getRoleByNome(String nome) {
        Role role = roleRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        return RoleDTO.fromEntity(role);
    }

    @Transactional
    public RoleDTO createRole(RoleCreateRequest request) {
        if (roleRepository.existsByNome(request.getNome())) {
            throw new RuntimeException("Já existe uma role com este nome");
        }

        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        }

        Role role = Role.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .sistema(false)
                .permissions(permissions)
                .build();

        role = roleRepository.save(role);
        log.info("Role criada: {} (ID: {})", role.getNome(), role.getId());
        
        return RoleDTO.fromEntity(role);
    }

    @Transactional
    public RoleDTO updateRole(Long id, RoleCreateRequest request) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        if (role.getSistema()) {
            throw new RuntimeException("Roles do sistema não podem ser editadas");
        }

        if (!role.getNome().equals(request.getNome()) && 
            roleRepository.existsByNome(request.getNome())) {
            throw new RuntimeException("Já existe uma role com este nome");
        }

        role.setNome(request.getNome());
        role.setDescricao(request.getDescricao());
        role.setAtivo(request.getAtivo());

        // Atualizar permissões
        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        }
        role.setPermissions(permissions);

        role = roleRepository.save(role);
        log.info("Role atualizada: {} (ID: {})", role.getNome(), role.getId());
        
        return RoleDTO.fromEntity(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        if (role.getSistema()) {
            throw new RuntimeException("Roles do sistema não podem ser deletadas");
        }

        roleRepository.delete(role);
        log.info("Role deletada: {} (ID: {})", role.getNome(), id);
    }

    @Transactional
    public void toggleRoleStatus(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        if (role.getSistema()) {
            throw new RuntimeException("Roles do sistema não podem ser desativadas");
        }

        role.setAtivo(!role.getAtivo());
        roleRepository.save(role);
        
        log.info("Status da role alterado: {} - Ativo: {}", role.getNome(), role.getAtivo());
    }

    @Transactional
    public RoleDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        Set<Permission> newPermissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.getPermissions().addAll(newPermissions);

        role = roleRepository.save(role);
        log.info("Permissões adicionadas à role: {}", role.getNome());
        
        return RoleDTO.fromEntity(role);
    }

    @Transactional
    public RoleDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        role.getPermissions().removeIf(p -> permissionIds.contains(p.getId()));

        role = roleRepository.save(role);
        log.info("Permissões removidas da role: {}", role.getNome());
        
        return RoleDTO.fromEntity(role);
    }
}

