package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByNome(String nome);
    
    List<Role> findByAtivoTrue();
    
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<Role> findByIdWithPermissions(Long id);
    
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.ativo = true")
    List<Role> findAllActiveWithPermissions();
    
    boolean existsByNome(String nome);
}
