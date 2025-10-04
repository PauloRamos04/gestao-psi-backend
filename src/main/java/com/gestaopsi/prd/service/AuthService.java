package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> authenticate(String clinicaLogin, String psicologLogin, String senha) {
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


