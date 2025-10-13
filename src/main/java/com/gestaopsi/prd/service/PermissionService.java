package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.PermissionDTO;
import com.gestaopsi.prd.entity.Permission;
import com.gestaopsi.prd.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(PermissionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> getActivePermissions() {
        return permissionRepository.findByAtivoTrue().stream()
                .map(PermissionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> getPermissionsByModulo(String modulo) {
        return permissionRepository.findByModulo(modulo).stream()
                .map(PermissionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllModulos() {
        return permissionRepository.findAllModulos();
    }

    @Transactional(readOnly = true)
    public List<String> getAllAcoes() {
        return permissionRepository.findAllAcoes();
    }

    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));
        return PermissionDTO.fromEntity(permission);
    }

    @Transactional
    public PermissionDTO createPermission(PermissionDTO dto) {
        if (permissionRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma permissão com este nome");
        }

        Permission permission = Permission.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .modulo(dto.getModulo())
                .acao(dto.getAcao())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();

        permission = permissionRepository.save(permission);
        log.info("Permissão criada: {} (ID: {})", permission.getNome(), permission.getId());
        
        return PermissionDTO.fromEntity(permission);
    }

    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

        if (!permission.getNome().equals(dto.getNome()) && 
            permissionRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma permissão com este nome");
        }

        permission.setNome(dto.getNome());
        permission.setDescricao(dto.getDescricao());
        permission.setModulo(dto.getModulo());
        permission.setAcao(dto.getAcao());
        permission.setAtivo(dto.getAtivo());

        permission = permissionRepository.save(permission);
        log.info("Permissão atualizada: {} (ID: {})", permission.getNome(), permission.getId());
        
        return PermissionDTO.fromEntity(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

        permissionRepository.delete(permission);
        log.info("Permissão deletada: {} (ID: {})", permission.getNome(), id);
    }

    @Transactional
    public void togglePermissionStatus(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

        permission.setAtivo(!permission.getAtivo());
        permissionRepository.save(permission);
        
        log.info("Status da permissão alterado: {} - Ativo: {}", permission.getNome(), permission.getAtivo());
    }
}


