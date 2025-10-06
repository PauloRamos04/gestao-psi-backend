package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.UsuarioRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.Role;
import com.gestaopsi.prd.entity.TipoUser;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.RoleRepository;
import com.gestaopsi.prd.repository.TipoUserRepository;
import com.gestaopsi.prd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;
    private final TipoUserRepository tipoUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario criar(UsuarioRequest request) {
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
                .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        TipoUser tipo = tipoUserRepository.findById(request.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de usuário não encontrado"));
        
        Role role = null;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId()).orElse(null);
        }
        
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .clinica(clinica)
                .psicologo(psicologo)
                .tipo(tipo)
                .role(role)
                .senha(passwordEncoder.encode(request.getSenha()))
                .titulo(request.getTitulo())
                .status(request.getStatus() != null ? request.getStatus() : true)
                .build();
        
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Optional<Usuario> atualizar(Long id, UsuarioRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (request.getClinicaId() != null) {
                Clinica clinica = clinicaRepository.findById(request.getClinicaId())
                        .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
                usuario.setClinica(clinica);
            }
            
            if (request.getPsicologId() != null) {
                Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
                        .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
                usuario.setPsicologo(psicologo);
            }
            
            if (request.getTipoId() != null) {
                TipoUser tipo = tipoUserRepository.findById(request.getTipoId())
                        .orElseThrow(() -> new IllegalArgumentException("Tipo de usuário não encontrado"));
                usuario.setTipo(tipo);
            }
            
            if (request.getRoleId() != null) {
                Role role = roleRepository.findById(request.getRoleId()).orElse(null);
                usuario.setRole(role);
            }
            
            if (request.getSenha() != null && !request.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            }
            
            if (request.getTitulo() != null) {
                usuario.setTitulo(request.getTitulo());
            }
            
            if (request.getStatus() != null) {
                usuario.setStatus(request.getStatus());
            }
            
            return usuarioRepository.save(usuario);
        });
    }

    public boolean ativar(Long id) {
        return usuarioRepository.ativar(id) > 0;
    }

    public boolean desativar(Long id) {
        return usuarioRepository.desativar(id) > 0;
    }
}


