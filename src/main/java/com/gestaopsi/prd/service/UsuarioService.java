package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    public boolean ativar(Long id) {
        return usuarioRepository.ativar(id) > 0;
    }

    public boolean desativar(Long id) {
        return usuarioRepository.desativar(id) > 0;
    }
}


