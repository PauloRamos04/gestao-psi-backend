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

    @Transactional(readOnly = true)
    public List<Psicologo> listAll() {
        log.info("Listando todos os psicólogos");
        // Usar fetch join para evitar LazyInitializationException
        return psicologoRepository.findAllWithCategoria();
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
        
        Psicologo.PsicologoBuilder builder = Psicologo.builder()
                .psicologLogin(psicologLogin)
                .nome(request.getNome())
                .crp(request.getCrp())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .dtAtivacao(request.getDtAtivacao() != null ? request.getDtAtivacao() : LocalDate.now())
                .categoria(categoria);
        
        // Documentos
        if (request.getCpf() != null) builder.cpf(request.getCpf());
        if (request.getRg() != null) builder.rg(request.getRg());
        
        // Contato
        if (request.getCelular() != null) builder.celular(request.getCelular());
        if (request.getTelefoneEmergencia() != null) builder.telefoneEmergencia(request.getTelefoneEmergencia());
        if (request.getContatoEmergenciaNome() != null) builder.contatoEmergenciaNome(request.getContatoEmergenciaNome());
        
        // Dados Pessoais
        if (request.getDataNascimento() != null) builder.dataNascimento(request.getDataNascimento());
        if (request.getGenero() != null) builder.genero(request.getGenero());
        if (request.getEstadoCivil() != null) builder.estadoCivil(request.getEstadoCivil());
        if (request.getNacionalidade() != null) builder.nacionalidade(request.getNacionalidade());
        
        // Endereço
        if (request.getCep() != null) builder.cep(request.getCep());
        if (request.getLogradouro() != null) builder.logradouro(request.getLogradouro());
        if (request.getNumeroEndereco() != null) builder.numeroEndereco(request.getNumeroEndereco());
        if (request.getComplemento() != null) builder.complemento(request.getComplemento());
        if (request.getBairro() != null) builder.bairro(request.getBairro());
        if (request.getCidade() != null) builder.cidade(request.getCidade());
        if (request.getEstado() != null) builder.estado(request.getEstado());
        
        // Formação
        if (request.getFormacaoAcademica() != null) builder.formacaoAcademica(request.getFormacaoAcademica());
        if (request.getEspecializacoes() != null) builder.especializacoes(request.getEspecializacoes());
        if (request.getAbordagemTerapeutica() != null) builder.abordagemTerapeutica(request.getAbordagemTerapeutica());
        if (request.getAreasAtuacao() != null) builder.areasAtuacao(request.getAreasAtuacao());
        if (request.getAnosExperiencia() != null) builder.anosExperiencia(request.getAnosExperiencia());
        if (request.getUniversidadeFormacao() != null) builder.universidadeFormacao(request.getUniversidadeFormacao());
        if (request.getAnoFormacao() != null) builder.anoFormacao(request.getAnoFormacao());
        
        // Profissional
        if (request.getDuracaoSessaoMinutos() != null) builder.duracaoSessaoMinutos(request.getDuracaoSessaoMinutos());
        if (request.getAceitaConvenio() != null) builder.aceitaConvenio(request.getAceitaConvenio());
        if (request.getConveniosAceitos() != null) builder.conveniosAceitos(request.getConveniosAceitos());
        if (request.getObservacoes() != null) builder.observacoes(request.getObservacoes());
        if (request.getBio() != null) builder.bio(request.getBio());
        if (request.getFotoUrl() != null) builder.fotoUrl(request.getFotoUrl());
        if (request.getAtivo() != null) builder.ativo(request.getAtivo());
        
        Psicologo psicologo = builder.build();
        
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
                    if (request.getCpf() != null) psicologo.setCpf(request.getCpf());
                    if (request.getRg() != null) psicologo.setRg(request.getRg());
                    psicologo.setCrp(request.getCrp());
                    psicologo.setEmail(request.getEmail());
                    psicologo.setTelefone(request.getTelefone());
                    if (request.getCelular() != null) psicologo.setCelular(request.getCelular());
                    if (request.getTelefoneEmergencia() != null) psicologo.setTelefoneEmergencia(request.getTelefoneEmergencia());
                    if (request.getContatoEmergenciaNome() != null) psicologo.setContatoEmergenciaNome(request.getContatoEmergenciaNome());
                    
                    if (request.getDataNascimento() != null) psicologo.setDataNascimento(request.getDataNascimento());
                    if (request.getGenero() != null) psicologo.setGenero(request.getGenero());
                    if (request.getEstadoCivil() != null) psicologo.setEstadoCivil(request.getEstadoCivil());
                    if (request.getNacionalidade() != null) psicologo.setNacionalidade(request.getNacionalidade());
                    
                    if (request.getCep() != null) psicologo.setCep(request.getCep());
                    if (request.getLogradouro() != null) psicologo.setLogradouro(request.getLogradouro());
                    if (request.getNumeroEndereco() != null) psicologo.setNumeroEndereco(request.getNumeroEndereco());
                    if (request.getComplemento() != null) psicologo.setComplemento(request.getComplemento());
                    if (request.getBairro() != null) psicologo.setBairro(request.getBairro());
                    if (request.getCidade() != null) psicologo.setCidade(request.getCidade());
                    if (request.getEstado() != null) psicologo.setEstado(request.getEstado());
                    
                    if (request.getFormacaoAcademica() != null) psicologo.setFormacaoAcademica(request.getFormacaoAcademica());
                    if (request.getEspecializacoes() != null) psicologo.setEspecializacoes(request.getEspecializacoes());
                    if (request.getAbordagemTerapeutica() != null) psicologo.setAbordagemTerapeutica(request.getAbordagemTerapeutica());
                    if (request.getAreasAtuacao() != null) psicologo.setAreasAtuacao(request.getAreasAtuacao());
                    if (request.getAnosExperiencia() != null) psicologo.setAnosExperiencia(request.getAnosExperiencia());
                    if (request.getUniversidadeFormacao() != null) psicologo.setUniversidadeFormacao(request.getUniversidadeFormacao());
                    if (request.getAnoFormacao() != null) psicologo.setAnoFormacao(request.getAnoFormacao());
                    
                    if (request.getDtAtivacao() != null) {
                        psicologo.setDtAtivacao(request.getDtAtivacao());
                    }
                    if (request.getDuracaoSessaoMinutos() != null) psicologo.setDuracaoSessaoMinutos(request.getDuracaoSessaoMinutos());
                    if (request.getAceitaConvenio() != null) psicologo.setAceitaConvenio(request.getAceitaConvenio());
                    if (request.getConveniosAceitos() != null) psicologo.setConveniosAceitos(request.getConveniosAceitos());
                    if (request.getObservacoes() != null) psicologo.setObservacoes(request.getObservacoes());
                    if (request.getBio() != null) psicologo.setBio(request.getBio());
                    if (request.getFotoUrl() != null) psicologo.setFotoUrl(request.getFotoUrl());
                    if (request.getAtivo() != null) psicologo.setAtivo(request.getAtivo());
                    
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


