package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.PermissionDTO;
import com.gestaopsi.prd.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PermissionDTO>> getActivePermissions() {
        return ResponseEntity.ok(permissionService.getActivePermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping("/modulo/{modulo}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByModulo(@PathVariable String modulo) {
        return ResponseEntity.ok(permissionService.getPermissionsByModulo(modulo));
    }

    @GetMapping("/modulos")
    public ResponseEntity<List<String>> getAllModulos() {
        return ResponseEntity.ok(permissionService.getAllModulos());
    }

    @GetMapping("/acoes")
    public ResponseEntity<List<String>> getAllAcoes() {
        return ResponseEntity.ok(permissionService.getAllAcoes());
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.createPermission(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, dto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> togglePermissionStatus(@PathVariable Long id) {
        permissionService.togglePermissionStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}

