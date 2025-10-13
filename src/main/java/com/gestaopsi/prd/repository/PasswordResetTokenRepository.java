package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.PasswordResetToken;
import com.gestaopsi.prd.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByUsuarioAndUsadoFalseAndExpiryDateAfter(
        Usuario usuario, LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiryDate < :now")
    void deleteExpiredTokens(LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.usuario = :usuario")
    void deleteByUsuario(Usuario usuario);
}


