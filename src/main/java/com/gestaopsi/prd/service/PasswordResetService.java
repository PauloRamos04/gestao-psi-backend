package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.PasswordResetToken;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.exception.InvalidTokenException;
import com.gestaopsi.prd.exception.ResourceNotFoundException;
import com.gestaopsi.prd.exception.TokenExpiredException;
import com.gestaopsi.prd.repository.PasswordResetTokenRepository;
import com.gestaopsi.prd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    private static final int EXPIRATION_HOURS = 24;

    @Transactional
    public void createPasswordResetToken(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
        
        // Deleta tokens anteriores do usuário
        tokenRepository.deleteByUsuario(usuario);
        
        // Cria novo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(token)
            .usuario(usuario)
            .expiryDate(LocalDateTime.now().plusHours(EXPIRATION_HOURS))
            .usado(false)
            .build();
        
        tokenRepository.save(resetToken);
        
        // Envia email
        String userName = usuario.getNomeCompleto() != null ? usuario.getNomeCompleto() : usuario.getUsername();
        emailService.sendPasswordResetEmail(usuario.getEmail(), userName, token);
        
        log.info("Token de reset de senha criado para o usuário: {}", email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new InvalidTokenException("Token inválido"));
        
        if (resetToken.getUsado()) {
            throw new InvalidTokenException("Token já foi utilizado");
        }
        
        if (resetToken.isExpired()) {
            throw new TokenExpiredException("Token expirado");
        }
        
        // Atualiza senha
        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        
        // Marca token como usado
        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
        
        log.info("Senha resetada com sucesso para o usuário: {}", usuario.getEmail());
    }

    public boolean validateToken(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));
            
            return !resetToken.getUsado() && !resetToken.isExpired();
        } catch (Exception e) {
            return false;
        }
    }

    @Scheduled(cron = "0 0 2 * * ?") // Executa às 2h da manhã todos os dias
    @Transactional
    public void deleteExpiredTokens() {
        log.info("Iniciando limpeza de tokens expirados");
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Limpeza de tokens expirados concluída");
    }
}

