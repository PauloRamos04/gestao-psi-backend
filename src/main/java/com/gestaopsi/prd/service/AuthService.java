package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Novo método de autenticação por username
    public Optional<Usuario> authenticate(String username, String senha) {
        log.info("Tentando autenticar usuário: {}", username);
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameAndStatusTrue(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                log.info("Autenticação bem-sucedida para: {}", username);
                return Optional.of(usuario);
            }
            log.warn("Senha incorreta para: {}", username);
        } else {
            log.warn("Usuário não encontrado: {}", username);
        }
        return Optional.empty();
    }

    // Método antigo mantido para compatibilidade temporária
    @Deprecated
    public Optional<Usuario> authenticateLegacy(String clinicaLogin, String psicologLogin, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findActiveUserByLogins(clinicaLogin, psicologLogin);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public String hashPassword(String senha) { 
        return passwordEncoder.encode(senha); 
    }
}


