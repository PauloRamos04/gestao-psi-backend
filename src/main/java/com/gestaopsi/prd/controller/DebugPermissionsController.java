package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Permission;
import com.gestaopsi.prd.entity.Role;
import com.gestaopsi.prd.repository.PermissionRepository;
import com.gestaopsi.prd.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DebugPermissionsController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Debug endpoint funcionando!");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/permissions/count")
    public Map<String, Object> getPermissionsCount() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Contar permissões
            long totalPermissions = permissionRepository.count();
            long activePermissions = permissionRepository.findByAtivoTrue().size();
            
            result.put("totalPermissions", totalPermissions);
            result.put("activePermissions", activePermissions);
            
            // Contar roles
            long totalRoles = roleRepository.count();
            long activeRoles = roleRepository.findByAtivoTrue().size();
            
            result.put("totalRoles", totalRoles);
            result.put("activeRoles", activeRoles);
            
            log.info("📊 Permissões: {} total, {} ativas", totalPermissions, activePermissions);
            log.info("📊 Roles: {} total, {} ativas", totalRoles, activeRoles);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            log.error("❌ Erro ao contar permissões: {}", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/roles/with-permissions")
    public Map<String, Object> getRolesWithPermissions() {
        Map<String, Object> result = new HashMap<>();
        
        List<Role> roles = roleRepository.findAll();
        
        for (Role role : roles) {
            Role roleWithPermissions = roleRepository.findByIdWithPermissions(role.getId()).orElse(role);
            result.put(role.getNome(), Map.of(
                "id", role.getId(),
                "nome", role.getNome(),
                "ativo", role.getAtivo(),
                "sistema", role.getSistema(),
                "permissionsCount", roleWithPermissions.getPermissions().size(),
                "permissions", roleWithPermissions.getPermissions().stream()
                    .map(Permission::getNome)
                    .toList()
            ));
        }
        
        return result;
    }

    @PostMapping("/permissions/assign-all-to-admin")
    public Map<String, Object> assignAllPermissionsToAdmin() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Buscar role ADMIN
            Role adminRole = roleRepository.findByNome("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada"));
            
            // Buscar todas as permissões
            List<Permission> allPermissions = permissionRepository.findAll();
            
            // Atribuir todas as permissões ao ADMIN
            adminRole.setPermissions(new java.util.HashSet<>(allPermissions));
            roleRepository.save(adminRole);
            
            // Verificar
            Role adminWithPermissions = roleRepository.findByIdWithPermissions(adminRole.getId()).orElse(adminRole);
            
            result.put("success", true);
            result.put("adminPermissionsCount", adminWithPermissions.getPermissions().size());
            result.put("totalPermissions", allPermissions.size());
            
            log.info("✅ Todas as {} permissões atribuídas ao ADMIN", adminWithPermissions.getPermissions().size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            log.error("❌ Erro ao atribuir permissões ao ADMIN: {}", e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/permissions/force-assign-all")
    public Map<String, Object> forceAssignAllPermissions() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("🚀 Iniciando atribuição forçada de permissões...");
            
            // Buscar todas as permissões
            List<Permission> allPermissions = permissionRepository.findAll();
            log.info("📊 Total de permissões encontradas: {}", allPermissions.size());
            
            // Buscar todas as roles
            List<Role> allRoles = roleRepository.findAll();
            log.info("📊 Total de roles encontradas: {}", allRoles.size());
            
            Map<String, Integer> assignments = new HashMap<>();
            
            // Atribuir permissões baseado no tipo de role
            for (Role role : allRoles) {
                List<Permission> permissionsToAssign = new ArrayList<>();
                
                switch (role.getNome()) {
                    case "ADMIN":
                        permissionsToAssign = allPermissions; // Todas as permissões
                        break;
                    case "PSICOLOGO":
                        permissionsToAssign = allPermissions.stream()
                            .filter(p -> List.of("pacientes", "sessoes", "prontuarios", "agenda", "ferramentas", "perfil")
                                .contains(p.getModulo()))
                            .collect(Collectors.toList());
                        break;
                    case "FUNCIONARIO":
                        permissionsToAssign = allPermissions.stream()
                            .filter(p -> (p.getModulo().equals("pacientes") && p.getAcao().equals("ler")) ||
                                       (p.getModulo().equals("sessoes") && List.of("ler", "criar").contains(p.getAcao())) ||
                                       (p.getModulo().equals("agenda") && p.getAcao().equals("ler")) ||
                                       (p.getModulo().equals("perfil") && List.of("ler", "editar").contains(p.getAcao())))
                            .collect(Collectors.toList());
                        break;
                    case "SECRETARIA":
                        permissionsToAssign = allPermissions.stream()
                            .filter(p -> List.of("pacientes", "sessoes", "pagamentos", "agenda", "mensagens", "relatorios", "perfil")
                                .contains(p.getModulo()))
                            .collect(Collectors.toList());
                        break;
                }
                
                // Atribuir permissões à role
                if (!permissionsToAssign.isEmpty()) {
                    role.setPermissions(new java.util.HashSet<>(permissionsToAssign));
                    roleRepository.save(role);
                    assignments.put(role.getNome(), permissionsToAssign.size());
                    log.info("✅ Role {}: {} permissões atribuídas", role.getNome(), permissionsToAssign.size());
                }
            }
            
            result.put("success", true);
            result.put("assignments", assignments);
            result.put("totalPermissions", allPermissions.size());
            
            log.info("🎉 Atribuição forçada concluída com sucesso!");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            log.error("❌ Erro na atribuição forçada: {}", e.getMessage(), e);
        }
        
        return result;
    }
}
