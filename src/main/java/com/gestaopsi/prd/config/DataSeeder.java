package com.gestaopsi.prd.config;

import com.gestaopsi.prd.entity.*;
import com.gestaopsi.prd.repository.*;
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
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            ClinicaRepository clinicaRepository,
            PsicologoRepository psicologoRepository,
            UsuarioRepository usuarioRepository,
            TipoUserRepository tipoUserRepository,
            CategoriaRepository categoriaRepository,
            PasswordEncoder passwordEncoder) {
        this.clinicaRepository = clinicaRepository;
        this.psicologoRepository = psicologoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoUserRepository = tipoUserRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            logger.info("🌱 Iniciando seeder de dados...");

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

        // 2. Criar Categorias
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

        // 3. Criar Clínica
        Clinica clinica = Clinica.builder()
            .clinicaLogin("clinica1")
            .nome("Clínica Gestão PSI")
            .status(true)
            .titulo("Clínica Principal")
            .build();
        clinica = clinicaRepository.save(clinica);
        logger.info("✅ Clínica criada: {} (ID: {})", clinica.getNome(), clinica.getId());

        // 4. Criar Psicólogo
        Psicologo psicologo = Psicologo.builder()
            .psicologLogin("psicologo1")
            .nome("Dr. Psicólogo Teste")
            .dtAtivacao(LocalDate.now())
            .categoria(categoria)
            .build();
        psicologo = psicologoRepository.save(psicologo);
        logger.info("✅ Psicólogo criado: {} (ID: {})", psicologo.getNome(), psicologo.getId());

        // 5. Criar Usuário Administrador
        Usuario usuario = Usuario.builder()
            .username("admin")
            .clinica(clinica)
            .psicologo(psicologo)
            .tipo(tipoAdmin)
            .senha(passwordEncoder.encode("senha123"))
            .status(true)
            .titulo("Administrador do Sistema")
            .build();
        usuario = usuarioRepository.save(usuario);
        logger.info("✅ Usuário criado: {} (ID: {})", usuario.getUsername(), usuario.getId());

        logger.info("");
        logger.info("=================================================");
        logger.info("🎉 DADOS INICIAIS CRIADOS COM SUCESSO!");
        logger.info("=================================================");
        logger.info("📋 Credenciais de acesso:");
        logger.info("   Username: admin");
        logger.info("   Senha: senha123");
        logger.info("");
        logger.info("🏥 Clínica: {} (login: {})", clinica.getNome(), clinica.getClinicaLogin());
        logger.info("👨‍⚕️ Psicólogo: {} (login: {})", psicologo.getNome(), psicologo.getPsicologLogin());
        logger.info("=================================================");
        logger.info("");
    }
}
