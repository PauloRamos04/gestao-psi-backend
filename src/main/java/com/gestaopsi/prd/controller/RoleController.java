package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.RoleCreateRequest;
import com.gestaopsi.prd.dto.RoleDTO;
import com.gestaopsi.prd.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/active")
    public ResponseEntity<List<RoleDTO>> getActiveRoles() {
        return ResponseEntity.ok(roleService.getActiveRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<RoleDTO> getRoleByNome(@PathVariable String nome) {
        return ResponseEntity.ok(roleService.getRoleByNome(nome));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRole(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleCreateRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleRoleStatus(@PathVariable Long id) {
        roleService.toggleRoleStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions/add")
    public ResponseEntity<RoleDTO> addPermissionsToRole(
            @PathVariable Long id,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(id, permissionIds));
    }

    @PostMapping("/{id}/permissions/remove")
    public ResponseEntity<RoleDTO> removePermissionsFromRole(
            @PathVariable Long id,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(id, permissionIds));
    }
}


