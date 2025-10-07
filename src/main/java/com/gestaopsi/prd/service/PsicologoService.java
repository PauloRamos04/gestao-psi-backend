package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.PsicologoRequest;
import com.gestaopsi.prd.entity.Categoria;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.TipoUser;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.CategoriaRepository;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.TipoUserRepository;
import com.gestaopsi.prd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PsicologoService {

    private final PsicologoRepository psicologoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final TipoUserRepository tipoUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Psicologo> listAll() {
        log.info("Listando todos os psicólogos");
        return psicologoRepository.findAll();
    }

    public Optional<Psicologo> findByLogin(String login) {
        return psicologoRepository.findByPsicologLogin(login);
    }

    @Transactional
    public Psicologo criar(PsicologoRequest request) {
        log.info("Criando novo psicólogo: {}", request.getNome());
        
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        // Gerar psicologLogin automático se não fornecido
        String psicologLogin = request.getPsicologLogin();
        if (psicologLogin == null || psicologLogin.isBlank()) {
            // Gerar login baseado no nome (ex: "joao.silva")
            psicologLogin = gerarLoginAutomatico(request.getNome());
        }
        
        Psicologo psicologo = Psicologo.builder()
                .psicologLogin(psicologLogin)
                .nome(request.getNome())
                .crp(request.getCrp())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .dtAtivacao(request.getDtAtivacao() != null ? request.getDtAtivacao() : LocalDate.now())
                .categoria(categoria)
                .build();
        
        psicologo = psicologoRepository.save(psicologo);
        
        // NOVO: Criar usuário automaticamente se dados fornecidos
        if (request.getUsername() != null && !request.getUsername().isBlank() &&
            request.getSenha() != null && !request.getSenha().isBlank()) {
            
            log.info("Criando usuário automaticamente para psicólogo: {}", psicologo.getId());
            criarUsuarioParaPsicologo(psicologo, request);
        }
        
        return psicologo;
    }
    
    /**
     * Cria usuário automaticamente para o psicólogo
     */
    private void criarUsuarioParaPsicologo(Psicologo psicologo, PsicologoRequest request) {
        // Buscar clínica (primeira disponível ou específica)
        Clinica clinica = clinicaRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Nenhuma clínica encontrada"));
        
        // Buscar tipo de usuário
        TipoUser tipoUser = null;
        if (request.getTipoUserId() != null) {
            tipoUser = tipoUserRepository.findById(request.getTipoUserId())
                .orElse(null);
        }
        
        // Se não fornecido, buscar tipo PSICOLOGO
        if (tipoUser == null) {
            tipoUser = tipoUserRepository.findAll().stream()
                .filter(t -> "PSICOLOGO".equals(t.getNome()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo PSICOLOGO não encontrado"));
        }
        
        Usuario usuario = Usuario.builder()
            .username(request.getUsername())
            .senha(passwordEncoder.encode(request.getSenha()))
            .clinica(clinica)
            .psicologo(psicologo)
            .tipo(tipoUser)
            .status(true)
            .titulo(psicologo.getNome())
            .build();
        
        usuarioRepository.save(usuario);
        log.info("✅ Usuário criado automaticamente: {}", usuario.getUsername());
    }
    
    /**
     * Gera login automático baseado no nome
     */
    private String gerarLoginAutomatico(String nome) {
        String base = nome.toLowerCase()
            .replaceAll("[^a-z0-9]", ".")
            .replaceAll("\\.+", ".")
            .replaceAll("^\\.|\\.$", "");
        
        // Verificar se já existe, adicionar número se necessário
        String login = base;
        int contador = 1;
        while (psicologoRepository.findByPsicologLogin(login).isPresent()) {
            login = base + contador++;
        }
        
        return login;
    }

    @Transactional
    public Optional<Psicologo> atualizar(Long id, PsicologoRequest request) {
        log.info("Atualizando psicólogo: {}", id);
        
        return psicologoRepository.findById(id)
                .map(psicologo -> {
                    if (request.getPsicologLogin() != null) {
                        psicologo.setPsicologLogin(request.getPsicologLogin());
                    }
                    
                    psicologo.setNome(request.getNome());
                    psicologo.setCrp(request.getCrp());
                    psicologo.setEmail(request.getEmail());
                    psicologo.setTelefone(request.getTelefone());
                    
                    if (request.getDtAtivacao() != null) {
                        psicologo.setDtAtivacao(request.getDtAtivacao());
                    }
                    
                    if (request.getCategoriaId() != null) {
                        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
                        psicologo.setCategoria(categoria);
                    }
                    
                    return psicologoRepository.save(psicologo);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando psicólogo: {}", id);
        return psicologoRepository.findById(id)
                .map(psicologo -> {
                    psicologoRepository.delete(psicologo);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Cria usuário para um psicólogo já existente
     */
    @Transactional
    public Usuario criarUsuarioParaPsicologoExistente(Long psicologoId, PsicologoRequest request) {
        log.info("Criando usuário para psicólogo existente: {}", psicologoId);
        
        Psicologo psicologo = psicologoRepository.findById(psicologoId)
            .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        // Verificar se já existe usuário para este psicólogo
        Optional<Usuario> usuarioExistente = usuarioRepository.findByPsicologoId(psicologoId);
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário para este psicólogo");
        }
        
        // Buscar clínica (primeira disponível ou específica)
        Clinica clinica = clinicaRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Nenhuma clínica encontrada"));
        
        // Buscar tipo de usuário
        TipoUser tipoUser = null;
        if (request.getTipoUserId() != null) {
            tipoUser = tipoUserRepository.findById(request.getTipoUserId())
                .orElse(null);
        }
        
        // Se não fornecido, buscar tipo PSICOLOGO
        if (tipoUser == null) {
            tipoUser = tipoUserRepository.findAll().stream()
                .filter(t -> "PSICOLOGO".equals(t.getNome()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo PSICOLOGO não encontrado"));
        }
        
        Usuario usuario = Usuario.builder()
            .username(request.getUsername())
            .senha(passwordEncoder.encode(request.getSenha()))
            .clinica(clinica)
            .psicologo(psicologo)
            .tipo(tipoUser)
            .status(true)
            .titulo(psicologo.getNome())
            .build();
        
        Usuario usuarioCriado = usuarioRepository.save(usuario);
        log.info("✅ Usuário criado para psicólogo existente: {}", usuario.getUsername());
        
        return usuarioCriado;
    }
    
    /**
     * Verifica se existe usuário para um psicólogo
     */
    public boolean temUsuario(Long psicologoId) {
        return usuarioRepository.findByPsicologoId(psicologoId).isPresent();
    }
}


