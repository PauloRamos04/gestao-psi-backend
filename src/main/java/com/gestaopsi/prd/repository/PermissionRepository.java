package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByNome(String nome);
    
    List<Permission> findByModulo(String modulo);
    
    List<Permission> findByAcao(String acao);
    
    List<Permission> findByModuloAndAcao(String modulo, String acao);
    
    List<Permission> findByAtivoTrue();
    
    @Query("SELECT DISTINCT p.modulo FROM Permission p WHERE p.ativo = true ORDER BY p.modulo")
    List<String> findAllModulos();
    
    @Query("SELECT DISTINCT p.acao FROM Permission p WHERE p.ativo = true ORDER BY p.acao")
    List<String> findAllAcoes();
    
    boolean existsByNome(String nome);
}

