package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Clinica;
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
    private final ClinicaService clinicaService;

    // Método de autenticação com validação obrigatória de clínica
    public Optional<Usuario> authenticate(String username, String senha, String clinicaLogin) {
        log.info("Tentando autenticar usuário: {} na clínica: {}", username, clinicaLogin);
        
        // Validar se clínica existe e está ativa (obrigatório)
        Optional<Clinica> clinicaOpt = clinicaService.findByLoginAtiva(clinicaLogin);
        if (clinicaOpt.isEmpty()) {
            log.warn("Clínica não encontrada ou inativa: {}", clinicaLogin);
            return Optional.empty();
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameAndStatusTrue(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Validar se o usuário pertence à clínica especificada (obrigatório)
            if (usuario.getClinica() == null || 
                !clinicaLogin.equals(usuario.getClinica().getClinicaLogin())) {
                log.warn("Usuário {} não pertence à clínica {}", username, clinicaLogin);
                return Optional.empty();
            }
            
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                log.info("Autenticação bem-sucedida para: {} na clínica: {}", 
                    username, usuario.getClinica().getClinicaLogin());
                return Optional.of(usuario);
            }
            log.warn("Senha incorreta para: {}", username);
        } else {
            log.warn("Usuário não encontrado: {}", username);
        }
        return Optional.empty();
    }

    // Método de autenticação simples (compatibilidade)
    public Optional<Usuario> authenticate(String username, String senha) {
        return authenticate(username, senha, null);
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


