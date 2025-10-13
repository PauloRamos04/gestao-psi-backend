package com.gestaopsi.prd.config;

import com.gestaopsi.prd.entity.*;
import com.gestaopsi.prd.repository.*;
import com.gestaopsi.prd.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"dev", "default"})  // Só executa em desenvolvimento, não em produção
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoUserRepository tipoUserRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PacienteRepository pacienteRepository;
    private final SalaRepository salaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemConfigService systemConfigService;

    public DataSeeder(
            ClinicaRepository clinicaRepository,
            PsicologoRepository psicologoRepository,
            UsuarioRepository usuarioRepository,
            TipoUserRepository tipoUserRepository,
            CategoriaRepository categoriaRepository,
            TipoPagamentoRepository tipoPagamentoRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PacienteRepository pacienteRepository,
            SalaRepository salaRepository,
            PasswordEncoder passwordEncoder,
            SystemConfigService systemConfigService) {
        this.clinicaRepository = clinicaRepository;
        this.psicologoRepository = psicologoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoUserRepository = tipoUserRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.pacienteRepository = pacienteRepository;
        this.salaRepository = salaRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemConfigService = systemConfigService;
    }

    @Override
    public void run(String... args) {
        try {
            logger.info("🌱 Iniciando seeder de dados...");

            // Inicializar configurações do sistema (sempre executa)
            logger.info("⚙️ Inicializando configurações do sistema...");
            systemConfigService.initializeDefaultConfigs();

            // Verificar se já existe dados
            if (clinicaRepository.count() > 0) {
                logger.info("✅ Dados já existem. Pulando seeder.");
                return;
            }

            logger.info("📝 Criando dados iniciais...");
            criarDadosIniciais();
        } catch (Exception e) {
            logger.error("❌ Erro ao executar seeder: {}", e.getMessage());
            logger.warn("⚠️ Aplicação continuará sem dados iniciais");
        }
    }

    private void criarDadosIniciais() {

        // 1. Criar Tipos de Usuário
        TipoUser tipoAdmin = TipoUser.builder()
            .nome("ADMIN")
            .build();
        tipoAdmin = tipoUserRepository.save(tipoAdmin);
        logger.info("✅ Tipo criado: ADMIN (ID: {})", tipoAdmin.getId());

        TipoUser tipoPsicologo = TipoUser.builder()
            .nome("PSICOLOGO")
            .build();
        tipoPsicologo = tipoUserRepository.save(tipoPsicologo);
        logger.info("✅ Tipo criado: PSICOLOGO (ID: {})", tipoPsicologo.getId());

        TipoUser tipoFuncionario = TipoUser.builder()
            .nome("FUNCIONARIO")
            .build();
        tipoFuncionario = tipoUserRepository.save(tipoFuncionario);
        logger.info("✅ Tipo criado: FUNCIONARIO (ID: {})", tipoFuncionario.getId());

        // 2. Criar Roles Padrões
        Role roleAdmin = Role.builder()
            .nome("ADMIN")
            .descricao("Administrador do sistema com acesso total")
            .ativo(true)
            .sistema(true)
            .build();
        roleAdmin = roleRepository.save(roleAdmin);
        logger.info("✅ Role criada: ADMIN (ID: {})", roleAdmin.getId());

        Role rolePsicologo = Role.builder()
            .nome("PSICOLOGO")
            .descricao("Psicólogo com acesso a pacientes e sessões")
            .ativo(true)
            .sistema(true)
            .build();
        rolePsicologo = roleRepository.save(rolePsicologo);
        logger.info("✅ Role criada: PSICOLOGO (ID: {})", rolePsicologo.getId());

        Role roleFuncionario = Role.builder()
            .nome("FUNCIONARIO")
            .descricao("Funcionário com acesso limitado")
            .ativo(true)
            .sistema(true)
            .build();
        roleFuncionario = roleRepository.save(roleFuncionario);
        logger.info("✅ Role criada: FUNCIONARIO (ID: {})", roleFuncionario.getId());

        Role roleSecretaria = Role.builder()
            .nome("SECRETARIA")
            .descricao("Secretária com acesso administrativo")
            .ativo(true)
            .sistema(true)
            .build();
        roleSecretaria = roleRepository.save(roleSecretaria);
        logger.info("✅ Role criada: SECRETARIA (ID: {})", roleSecretaria.getId());

        // 3. Criar Permissões Padrões
        criarPermissoesPadroes(roleAdmin, rolePsicologo, roleFuncionario, roleSecretaria);

        // 4. Criar Tipos de Pagamento
        TipoPagamento tipoPix = TipoPagamento.builder()
            .nome("PIX")
            .build();
        tipoPix = tipoPagamentoRepository.save(tipoPix);
        logger.info("✅ Tipo de pagamento criado: PIX (ID: {})", tipoPix.getId());

        TipoPagamento tipoDinheiro = TipoPagamento.builder()
            .nome("DINHEIRO")
            .build();
        tipoDinheiro = tipoPagamentoRepository.save(tipoDinheiro);
        logger.info("✅ Tipo de pagamento criado: DINHEIRO (ID: {})", tipoDinheiro.getId());

        TipoPagamento tipoCartao = TipoPagamento.builder()
            .nome("CARTAO_CREDITO")
            .build();
        tipoCartao = tipoPagamentoRepository.save(tipoCartao);
        logger.info("✅ Tipo de pagamento criado: CARTAO_CREDITO (ID: {})", tipoCartao.getId());

        TipoPagamento tipoDebito = TipoPagamento.builder()
            .nome("CARTAO_DEBITO")
            .build();
        tipoDebito = tipoPagamentoRepository.save(tipoDebito);
        logger.info("✅ Tipo de pagamento criado: CARTAO_DEBITO (ID: {})", tipoDebito.getId());

        TipoPagamento tipoTransferencia = TipoPagamento.builder()
            .nome("TRANSFERENCIA")
            .build();
        tipoTransferencia = tipoPagamentoRepository.save(tipoTransferencia);
        logger.info("✅ Tipo de pagamento criado: TRANSFERENCIA (ID: {})", tipoTransferencia.getId());

        // 3. Criar Categorias
        Categoria categoria = Categoria.builder()
            .nome("PSICOLOGO_CLINICO")
            .build();
        categoria = categoriaRepository.save(categoria);
        logger.info("✅ Categoria criada: PSICOLOGO_CLINICO (ID: {})", categoria.getId());

        Categoria categoriaOrg = Categoria.builder()
            .nome("PSICOLOGO_ORGANIZACIONAL")
            .build();
        categoriaOrg = categoriaRepository.save(categoriaOrg);

        Categoria categoriaEdu = Categoria.builder()
            .nome("PSICOLOGO_EDUCACIONAL")
            .build();
        categoriaEdu = categoriaRepository.save(categoriaEdu);

        Categoria categoriaNeuropsic = Categoria.builder()
            .nome("NEUROPSICOLOGIA")
            .build();
        categoriaNeuropsic = categoriaRepository.save(categoriaNeuropsic);

        // 4. Criar Clínicas de Teste
        Clinica clinicaAdmin = Clinica.builder()
            .clinicaLogin("admin")
            .nome("Clínica Admin")
            .status(true)
            .titulo("Clínica Administrativa")
            .build();
        clinicaAdmin = clinicaRepository.save(clinicaAdmin);
        logger.info("✅ Clínica criada: {} (login: {})", clinicaAdmin.getNome(), clinicaAdmin.getClinicaLogin());

        Clinica clinicaTeste = Clinica.builder()
            .clinicaLogin("teste")
            .nome("Clínica Teste")
            .status(true)
            .titulo("Clínica de Testes")
            .build();
        clinicaTeste = clinicaRepository.save(clinicaTeste);
        logger.info("✅ Clínica criada: {} (login: {})", clinicaTeste.getNome(), clinicaTeste.getClinicaLogin());

        // 5. Criar Psicólogos de Teste
        Psicologo psicologoAdmin = Psicologo.builder()
            .psicologLogin("admin")
            .nome("Dr. Admin Psicólogo")
            .crp("12345")
            .email("admin@gestaopsi.com")
            .telefone("(11) 99999-9999")
            .dtAtivacao(LocalDate.now())
            .categoria(categoria)
            .build();
        psicologoAdmin = psicologoRepository.save(psicologoAdmin);
        logger.info("✅ Psicólogo criado: {} (login: {})", psicologoAdmin.getNome(), psicologoAdmin.getPsicologLogin());

        Psicologo psicologoTeste = Psicologo.builder()
            .psicologLogin("teste")
            .nome("Dr. Teste Psicólogo")
            .crp("54321")
            .email("teste@gestaopsi.com")
            .telefone("(11) 88888-8888")
            .dtAtivacao(LocalDate.now())
            .categoria(categoria)
            .build();
        psicologoTeste = psicologoRepository.save(psicologoTeste);
        logger.info("✅ Psicólogo criado: {} (login: {})", psicologoTeste.getNome(), psicologoTeste.getPsicologLogin());

        // 6. Criar Usuários de Teste
        Usuario usuarioAdmin = Usuario.builder()
            .username("admin")
            .clinica(clinicaAdmin)
            .psicologo(psicologoAdmin)
            .tipo(tipoAdmin)
            .role(roleAdmin)
            .senha(passwordEncoder.encode("admin"))
            .status(true)
            .titulo("Administrador do Sistema")
            .nomeCompleto("Administrador Geral")
            .email("admin@gestaopsi.com")
            .telefone("(11) 99999-9999")
            .cargo("Administrador")
            .build();
        usuarioAdmin = usuarioRepository.save(usuarioAdmin);
        logger.info("✅ Usuário criado: {} (ID: {})", usuarioAdmin.getUsername(), usuarioAdmin.getId());

        Usuario usuarioTeste = Usuario.builder()
            .username("teste")
            .clinica(clinicaTeste)
            .psicologo(psicologoTeste)
            .tipo(tipoPsicologo)
            .role(rolePsicologo)
            .senha(passwordEncoder.encode("teste"))
            .status(true)
            .titulo("Psicólogo Teste")
            .nomeCompleto("Psicólogo de Teste")
            .email("teste@gestaopsi.com")
            .telefone("(11) 88888-8888")
            .cargo("Psicólogo")
            .build();
        usuarioTeste = usuarioRepository.save(usuarioTeste);
        logger.info("✅ Usuário criado: {} (ID: {})", usuarioTeste.getUsername(), usuarioTeste.getId());

        // 7. Criar Pacientes de Teste
        Paciente paciente1 = Paciente.builder()
            .nome("João Silva Santos")
            .cpf("12345678901")
            .email("joao.silva@email.com")
            .telefone("(11) 99999-1111")
            .celular("(11) 88888-1111")
            .dataNascimento(LocalDate.of(1990, 5, 15))
            .genero("MASCULINO")
            .estadoCivil("SOLTEIRO")
            .profissao("Engenheiro")
            .logradouro("Rua das Flores")
            .numeroEndereco("123")
            .bairro("Centro")
            .cidade("São Paulo")
            .estado("SP")
            .cep("01234-567")
            .clinica(clinicaAdmin)
            .psicologo(psicologoAdmin)
            .status(true)
            .build();
        paciente1 = pacienteRepository.save(paciente1);
        logger.info("✅ Paciente criado: {} (CPF: {})", paciente1.getNome(), paciente1.getCpf());

        Paciente paciente2 = Paciente.builder()
            .nome("Maria Oliveira Costa")
            .cpf("98765432100")
            .email("maria.oliveira@email.com")
            .telefone("(11) 99999-2222")
            .celular("(11) 88888-2222")
            .dataNascimento(LocalDate.of(1985, 8, 22))
            .genero("FEMININO")
            .estadoCivil("CASADA")
            .profissao("Psicóloga")
            .logradouro("Av. Paulista")
            .numeroEndereco("456")
            .bairro("Bela Vista")
            .cidade("São Paulo")
            .estado("SP")
            .cep("01310-100")
            .clinica(clinicaTeste)
            .psicologo(psicologoTeste)
            .status(true)
            .build();
        paciente2 = pacienteRepository.save(paciente2);
        logger.info("✅ Paciente criado: {} (CPF: {})", paciente2.getNome(), paciente2.getCpf());

        // 8. Criar Salas de Teste
        Sala sala1 = Sala.builder()
            .nome("Sala 1 - Individual")
            .numero("101")
            .capacidade(1)
            .clinica(clinicaAdmin)
            .ativa(true)
            .build();
        sala1 = salaRepository.save(sala1);
        logger.info("✅ Sala criada: {} (ID: {})", sala1.getNome(), sala1.getId());

        Sala sala2 = Sala.builder()
            .nome("Sala 2 - Grupo")
            .numero("201")
            .capacidade(8)
            .clinica(clinicaTeste)
            .ativa(true)
            .build();
        sala2 = salaRepository.save(sala2);
        logger.info("✅ Sala criada: {} (ID: {})", sala2.getNome(), sala2.getId());

        logger.info("");
        logger.info("=================================================");
        logger.info("🎉 DADOS INICIAIS CRIADOS COM SUCESSO!");
        logger.info("=================================================");
        logger.info("📋 Credenciais de acesso ADMIN:");
        logger.info("   Username: admin");
        logger.info("   Senha: admin");
        logger.info("   Clínica: admin");
        logger.info("");
        logger.info("📋 Credenciais de acesso TESTE:");
        logger.info("   Username: teste");
        logger.info("   Senha: teste");
        logger.info("   Clínica: teste");
        logger.info("");
        logger.info("🏥 Clínicas criadas:");
        logger.info("   - {} (login: {})", clinicaAdmin.getNome(), clinicaAdmin.getClinicaLogin());
        logger.info("   - {} (login: {})", clinicaTeste.getNome(), clinicaTeste.getClinicaLogin());
        logger.info("=================================================");
        logger.info("");
    }

    private void criarPermissoesPadroes(Role roleAdmin, Role rolePsicologo, Role roleFuncionario, Role roleSecretaria) {
        // Definir permissões essenciais
        String[] modulos = {"usuarios", "pacientes", "sessoes", "pagamentos", "prontuarios", "clinicas", "psicologos", "salas", "mensagens", "relatorios", "sistema", "permissoes", "roles", "agenda", "ferramentas", "perfil", "downloads", "uploads", "sublocacoes", "interacoes", "historico", "notificacoes", "auditoria"};
        String[] acoes = {"criar", "ler", "editar", "deletar", "ativar_desativar", "importar_exportar", "configurar", "backup", "logs", "auditoria", "gerenciar", "enviar", "exportar_pdf", "exportar_excel", "ler_geral", "ler_financeiro", "ler_timeline", "ler_pacientes_evolucao", "ler_financeiro_evolucao", "atribuir_permissoes", "acessar_calculadoras", "usar_temporizador", "gerenciar_cores", "trocar_senha", "gerenciar_preferencias", "solicitar", "listar", "baixar", "foto", "documento", "sugestoes", "indicacoes", "gerenciar_sala", "ler_salas", "notificacoes_gerenciar", "notificacoes_enviar", "cancelar", "anexar_documentos"};

        // Criar todas as permissões
        for (String modulo : modulos) {
            for (String acao : acoes) {
                String nome = modulo + "." + acao;
                Permission permission = Permission.builder()
                    .nome(nome)
                    .descricao("Permite " + acao + " " + modulo)
                    .modulo(modulo)
                    .acao(acao)
                    .ativo(true)
                    .build();
                permissionRepository.save(permission);
            }
        }

        // Atribuir permissões básicas às roles
        atribuirPermissoesBasicas(roleAdmin, rolePsicologo, roleFuncionario, roleSecretaria);

        logger.info("✅ Permissões básicas criadas e atribuídas");
        logger.info("   - {} módulos", modulos.length);
        logger.info("   - {} ações por módulo", acoes.length);
        logger.info("   - Total: {} permissões", modulos.length * acoes.length);
    }

    private void atribuirPermissoesBasicas(Role roleAdmin, Role rolePsicologo, Role roleFuncionario, Role roleSecretaria) {
        try {
            // Aguardar um pouco para garantir que as permissões foram salvas
            Thread.sleep(100);
            
            // Buscar todas as permissões criadas
            var todasPermissoes = permissionRepository.findAll();
            logger.info("📊 Total de permissões encontradas: {}", todasPermissoes.size());
            
            if (todasPermissoes.isEmpty()) {
                logger.warn("⚠️ Nenhuma permissão encontrada para atribuir às roles");
                return;
            }
            
            // ADMIN recebe todas as permissões
            var permissoesAdmin = new java.util.HashSet<>(todasPermissoes);
            roleAdmin.setPermissions(permissoesAdmin);
            roleAdmin = roleRepository.save(roleAdmin);
            logger.info("✅ ADMIN: {} permissões atribuídas", permissoesAdmin.size());
            
            // PSICOLOGO recebe permissões específicas
            var permissoesPsicologo = todasPermissoes.stream()
                .filter(p -> p.getModulo().equals("pacientes") || 
                           p.getModulo().equals("sessoes") || 
                           p.getModulo().equals("prontuarios") ||
                           p.getModulo().equals("agenda") ||
                           p.getModulo().equals("ferramentas") ||
                           p.getModulo().equals("perfil"))
                .collect(java.util.stream.Collectors.toSet());
            
            rolePsicologo.setPermissions(permissoesPsicologo);
            rolePsicologo = roleRepository.save(rolePsicologo);
            logger.info("✅ PSICOLOGO: {} permissões atribuídas", permissoesPsicologo.size());
            
            // FUNCIONARIO recebe permissões limitadas
            var permissoesFuncionario = todasPermissoes.stream()
                .filter(p -> (p.getModulo().equals("pacientes") && p.getAcao().equals("ler")) ||
                           (p.getModulo().equals("sessoes") && (p.getAcao().equals("ler") || p.getAcao().equals("criar"))) ||
                           (p.getModulo().equals("agenda") && p.getAcao().equals("ler")) ||
                           (p.getModulo().equals("perfil") && (p.getAcao().equals("ler") || p.getAcao().equals("editar"))))
                .collect(java.util.stream.Collectors.toSet());
            
            roleFuncionario.setPermissions(permissoesFuncionario);
            roleFuncionario = roleRepository.save(roleFuncionario);
            logger.info("✅ FUNCIONARIO: {} permissões atribuídas", permissoesFuncionario.size());
            
            // SECRETARIA recebe permissões administrativas
            var permissoesSecretaria = todasPermissoes.stream()
                .filter(p -> p.getModulo().equals("pacientes") || 
                           p.getModulo().equals("sessoes") || 
                           p.getModulo().equals("pagamentos") ||
                           p.getModulo().equals("agenda") ||
                           p.getModulo().equals("mensagens") ||
                           p.getModulo().equals("relatorios") ||
                           p.getModulo().equals("perfil"))
                .collect(java.util.stream.Collectors.toSet());
            
            roleSecretaria.setPermissions(permissoesSecretaria);
            roleSecretaria = roleRepository.save(roleSecretaria);
            logger.info("✅ SECRETARIA: {} permissões atribuídas", permissoesSecretaria.size());
            
            // Verificar se as permissões foram realmente atribuídas
            verificarPermissoesAtribuidas(roleAdmin, rolePsicologo, roleFuncionario, roleSecretaria);
            
        } catch (Exception e) {
            logger.error("❌ Erro ao atribuir permissões: {}", e.getMessage(), e);
        }
    }
    
    private void verificarPermissoesAtribuidas(Role roleAdmin, Role rolePsicologo, Role roleFuncionario, Role roleSecretaria) {
        try {
            // Buscar roles com permissões do banco
            var adminComPermissoes = roleRepository.findByIdWithPermissions(roleAdmin.getId());
            var psicologoComPermissoes = roleRepository.findByIdWithPermissions(rolePsicologo.getId());
            var funcionarioComPermissoes = roleRepository.findByIdWithPermissions(roleFuncionario.getId());
            var secretariaComPermissoes = roleRepository.findByIdWithPermissions(roleSecretaria.getId());
            
            logger.info("🔍 VERIFICAÇÃO FINAL:");
            logger.info("   ADMIN: {} permissões no banco", adminComPermissoes.map(r -> r.getPermissions().size()).orElse(0));
            logger.info("   PSICOLOGO: {} permissões no banco", psicologoComPermissoes.map(r -> r.getPermissions().size()).orElse(0));
            logger.info("   FUNCIONARIO: {} permissões no banco", funcionarioComPermissoes.map(r -> r.getPermissions().size()).orElse(0));
            logger.info("   SECRETARIA: {} permissões no banco", secretariaComPermissoes.map(r -> r.getPermissions().size()).orElse(0));
            
        } catch (Exception e) {
            logger.error("❌ Erro ao verificar permissões: {}", e.getMessage());
        }
    }

}
